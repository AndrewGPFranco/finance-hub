package com.agpf.finance.hub.services.subdomain;

import com.agpf.finance.hub.dtos.subdomain.EditSubdomainDTO;
import com.agpf.finance.hub.dtos.subdomain.OutputSubdomainDTO;
import com.agpf.finance.hub.dtos.subdomain.RegisterSubdomainDTO;
import com.agpf.finance.hub.enums.subdomain.PermissionSubdomainType;
import com.agpf.finance.hub.exceptions.BusinessException;
import com.agpf.finance.hub.exceptions.NotFoundException;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.subdomains.SubdomainRepository;
import com.agpf.finance.hub.utils.CrudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubdomainService {

    private final SubdomainRepository subdomainRepository;
    private final ClassPathResource pathPhoto = new ClassPathResource("src/main/resources/photos");

    @Transactional
    public void register(RegisterSubdomainDTO dto, User user) {
        var subdomainOptional = subdomainRepository.findByNameAndUser(dto.name(), user);

        if (subdomainOptional.isPresent())
            throw new BusinessException("Já há um subdomínio com o nome informado!");

        var urlPhoto = handlePhotoSubdomain(dto, user.getUsername());

        var entity = RegisterSubdomainDTO.toEntity(dto, user, urlPhoto);
        subdomainRepository.save(entity);
    }

    private String handlePhotoSubdomain(RegisterSubdomainDTO dto, String username) {
        if (dto.photo() == null || dto.photo().isEmpty())
            return dto.urlPhoto();

        var directory = Path.of(pathPhoto.getPath()).normalize();
        var fileName = sanitizeFileName(username)
                .concat("-").concat(sanitizeFileName(dto.name()))
                .concat(resolveFileExtension(dto.photo().getOriginalFilename()));

        var destination = directory.resolve(fileName).normalize();

        try (var inputStream = dto.photo().getInputStream()) {
            Files.createDirectories(directory);
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException io) {
            log.error(io.getMessage(), io);
            throw new RuntimeException("Não foi possível processar a imagem.");
        }

        return "/photos/".concat(fileName);
    }

    private String sanitizeFileName(String value) {
        return value == null ? "subdomain" : value.trim().replaceAll("[^a-zA-Z0-9._-]", "-");
    }

    private String resolveFileExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains("."))
            return ".png";

        var extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        return extension.matches("\\.(png|jpg|jpeg|webp|gif)") ? extension : ".png";
    }

    public List<OutputSubdomainDTO> subdomainsByUser(User user) {
        return subdomainRepository.subdomainsByUser(user.getId(), PermissionSubdomainType.EDITOR);
    }

    public boolean canManage(User user, UUID idSubdomain) {
        if (idSubdomain == null)
            return false;

        return subdomainRepository.findPermissionByIdAndUser(idSubdomain, user, PermissionSubdomainType.EDITOR)
                .filter(PermissionSubdomainType.EDITOR::equals).isPresent();
    }

    public UUID resolveSelectedSubdomainId(User user, UUID idSubdomain) {
        if (idSubdomain != null && subdomainRepository.findByIdAndUser(idSubdomain, user).isPresent())
            return idSubdomain;

        var subdomains = subdomainsByUser(user);

        if (subdomains.isEmpty())
            return null;

        return subdomains.stream().findFirst().map(OutputSubdomainDTO::id)
                .orElseThrow(() -> new NotFoundException("Subdomain não encontrado!"));
    }

    public OutputSubdomainDTO getSubdomainByIdAndUser(UUID idSubdomain, User user) {
        return OutputSubdomainDTO.fromEntity(resolve(user, idSubdomain), true);
    }

    @Transactional
    public void edit(EditSubdomainDTO dto, UUID idSubdomain, User user) {
        var subdomain = resolve(user, idSubdomain);

        CrudUtils.updateField(dto.name(), subdomain::setName);
        CrudUtils.updateField(dto.urlPhoto(), subdomain::setUrlPhoto);

        subdomainRepository.save(subdomain);
    }

    private Subdomain resolve(User user, UUID idSubdomain) {
        if (idSubdomain == null)
            throw new NotFoundException("Subdomínio não encontrado!");

        return subdomainRepository.findByIdAndUser(idSubdomain, user)
                .orElseThrow(() -> new NotFoundException("Subdomínio não encontrado!"));
    }

    @Transactional
    public void delete(UUID idSubdomain, User user) {
        var subdomain = resolve(user, idSubdomain);

        deleteLocalPhoto(subdomain.getUrlPhoto());
        subdomainRepository.delete(subdomain);
    }

    private void deleteLocalPhoto(String urlPhoto) {
        if (urlPhoto == null || urlPhoto.isBlank() || urlPhoto.startsWith("http://") || urlPhoto.startsWith("https://"))
            return;

        var fileName = urlPhoto.substring(urlPhoto.lastIndexOf("/") + 1);
        var photoPath = Path.of(pathPhoto.getPath()).normalize().resolve(fileName).normalize();

        try {
            Files.delete(photoPath);
        } catch (IOException io) {
            log.error(io.getMessage(), io);
            throw new BusinessException("Ocorreu um erro ao deletar a imagem do subdomínio, tente novamente!");
        }
    }
}
