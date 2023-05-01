package svobodavlad.web.rest.extended;

import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import svobodavlad.service.dto.PhotoDTO;
import svobodavlad.web.rest.PhotoResource;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PhotoExtendedResource {

    private final PhotoResource photoResource;

    @PostMapping("/photos")
    public ResponseEntity<PhotoDTO> createPhoto(@Valid @RequestBody PhotoDTO photoDTO) throws URISyntaxException {
        return photoResource.createPhoto(photoDTO);
    }

    @PutMapping("/photos/{id}")
    public ResponseEntity<PhotoDTO> updatePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhotoDTO photoDTO
    ) throws URISyntaxException {
        return photoResource.updatePhoto(id, photoDTO);
    }

    @PatchMapping(value = "/photos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhotoDTO> partialUpdatePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhotoDTO photoDTO
    ) throws URISyntaxException {
        return photoResource.partialUpdatePhoto(id, photoDTO);
    }

    @GetMapping("/photos")
    public ResponseEntity<List<PhotoDTO>> getAllPhotos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        return photoResource.getAllPhotos(pageable);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<PhotoDTO> getPhoto(@PathVariable Long id) {
        return photoResource.getPhoto(id);
    }

    @DeleteMapping("/photos/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        return photoResource.deletePhoto(id);
    }
}
