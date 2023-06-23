package it.uniroma3.siw.booking.controller.validator;

import it.uniroma3.siw.booking.model.Image;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
@Slf4j
public class ImageValidator implements Validator {


    @Value("${siwbooking.image.maxFileSize:#{300000}}")
    protected Long maxImageFileSize;


    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MultipartFile file = (MultipartFile) target;
        log.debug("Starting image file validation...");
        if (!isImage(file)) {
            log.debug("Uploaded file is not a valid image");
            errors.reject("artist.upload.file.is.not.a.valid.image");
        } else if (file.getSize() > maxImageFileSize) {
            log.debug("Uploaded image file exceeds the maximum size");
            errors.reject("image.upload.file.exceeds.max.size");
        }
    }


    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return Image.class.equals(aClass);
    }

    /**
     * Verifica se un MultiPartFile é una immagine
     *
     * @param file - MultipartFile
     * @return true se è una immagine, false altrimenti
     */
    private boolean isImage(MultipartFile file) {
        return isImage(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
    }


    /**
     * @param type - Mediatype
     * @return true se è una immagine, false altrimenti
     */
    private boolean isImage(MediaType type) {
        if (type == null) {
            return false;
        }

        return type.equals(MediaType.IMAGE_JPEG) ||
                type.equals(MediaType.IMAGE_PNG) ||
                type.equals(MediaType.IMAGE_GIF);
    }


}
