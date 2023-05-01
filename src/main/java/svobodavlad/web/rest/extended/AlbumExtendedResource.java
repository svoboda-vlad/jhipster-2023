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
import svobodavlad.service.dto.AlbumDTO;
import svobodavlad.web.rest.AlbumResource;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AlbumExtendedResource {

    private final AlbumResource albumResource;

    @PostMapping("/albums")
    public ResponseEntity<AlbumDTO> createAlbum(@Valid @RequestBody AlbumDTO albumDTO) throws URISyntaxException {
        return albumResource.createAlbum(albumDTO);
    }

    @PutMapping("/albums/{id}")
    public ResponseEntity<AlbumDTO> updateAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlbumDTO albumDTO
    ) throws URISyntaxException {
        return albumResource.updateAlbum(id, albumDTO);
    }

    @PatchMapping(value = "/albums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlbumDTO> partialUpdateAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlbumDTO albumDTO
    ) throws URISyntaxException {
        return albumResource.partialUpdateAlbum(id, albumDTO);
    }

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumDTO>> getAllAlbums(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        return albumResource.getAllAlbums(pageable);
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable Long id) {
        return albumResource.getAlbum(id);
    }

    @DeleteMapping("/albums/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        return albumResource.deleteAlbum(id);
    }
}
