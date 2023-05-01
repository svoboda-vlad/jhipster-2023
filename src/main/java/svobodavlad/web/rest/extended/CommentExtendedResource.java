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
import svobodavlad.service.dto.CommentDTO;
import svobodavlad.web.rest.CommentResource;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentExtendedResource {

    private final CommentResource commentResource;

    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) throws URISyntaxException {
        return commentResource.createComment(commentDTO);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommentDTO commentDTO
    ) throws URISyntaxException {
        return commentResource.updateComment(id, commentDTO);
    }

    @PatchMapping(value = "/comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentDTO> partialUpdateComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommentDTO commentDTO
    ) throws URISyntaxException {
        return commentResource.partialUpdateComment(id, commentDTO);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        return commentResource.getAllComments(pageable);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long id) {
        return commentResource.getComment(id);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        return commentResource.deleteComment(id);
    }
}
