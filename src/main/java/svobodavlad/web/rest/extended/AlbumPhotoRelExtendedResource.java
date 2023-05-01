package svobodavlad.web.rest.extended;

import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import svobodavlad.service.dto.AlbumPhotoRelDTO;
import svobodavlad.web.rest.AlbumPhotoRelResource;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AlbumPhotoRelExtendedResource {

    private final AlbumPhotoRelResource albumPhotoRelResource;

    @PostMapping("/album-photo-rels")
    public ResponseEntity<AlbumPhotoRelDTO> createAlbumPhotoRel(@RequestBody AlbumPhotoRelDTO albumPhotoRelDTO) throws URISyntaxException {
        return albumPhotoRelResource.createAlbumPhotoRel(albumPhotoRelDTO);
    }

    @PutMapping("/album-photo-rels/{id}")
    public ResponseEntity<AlbumPhotoRelDTO> updateAlbumPhotoRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlbumPhotoRelDTO albumPhotoRelDTO
    ) throws URISyntaxException {
        return albumPhotoRelResource.updateAlbumPhotoRel(id, albumPhotoRelDTO);
    }

    @PatchMapping(value = "/album-photo-rels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlbumPhotoRelDTO> partialUpdateAlbumPhotoRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlbumPhotoRelDTO albumPhotoRelDTO
    ) throws URISyntaxException {
        return albumPhotoRelResource.partialUpdateAlbumPhotoRel(id, albumPhotoRelDTO);
    }

    @GetMapping("/album-photo-rels")
    public ResponseEntity<List<AlbumPhotoRelDTO>> getAllAlbumPhotoRels(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        return albumPhotoRelResource.getAllAlbumPhotoRels(pageable);
    }

    @GetMapping("/album-photo-rels/{id}")
    public ResponseEntity<AlbumPhotoRelDTO> getAlbumPhotoRel(@PathVariable Long id) {
        return albumPhotoRelResource.getAlbumPhotoRel(id);
    }

    @DeleteMapping("/album-photo-rels/{id}")
    public ResponseEntity<Void> deleteAlbumPhotoRel(@PathVariable Long id) {
        return albumPhotoRelResource.deleteAlbumPhotoRel(id);
    }
}
