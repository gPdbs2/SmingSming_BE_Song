package com.smingsming.song.entity.artist.contoller;

import com.smingsming.song.entity.album.vo.AlbumVo;
import com.smingsming.song.entity.artist.entity.ArtistEntity;
import com.smingsming.song.entity.artist.vo.ArtistAddReqVo;
import com.smingsming.song.entity.artist.service.IArtistService;
import com.smingsming.song.entity.artist.vo.ArtistVo;
import com.smingsming.song.entity.song.vo.SongGetVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {
    private final IArtistService iArtistService;

    // 아티스트 추가
    @PostMapping(value = "/add")
    public ResponseEntity<?> addArtist(@RequestBody ArtistAddReqVo artistVo) {
        ArtistEntity result = iArtistService.addArtist(artistVo);

        if(result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가 실패");
    }

    // 아티스트 정보 조회
    @GetMapping(value = "/get/{artistId}")
    public ResponseEntity<?> getArtist(@PathVariable("artistId") Long artistId) {
        ArtistEntity result = iArtistService.getArtist(artistId);

        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("없는 아티스트 번호입니다.");
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> artistSearch(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                          @RequestParam(name = "page", defaultValue = "1") int page) {
        List<ArtistVo> result = iArtistService.artistSearch(keyword, page);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 아티스트별 앨범 조회
    @GetMapping(value = "/get/album/{artistId}")
    public ResponseEntity<?> getArtistAlbum(@PathVariable(name = "artistId") Long artistId,
                                            @RequestParam(name = "page", defaultValue = "1") int page) {
        List<AlbumVo> result = iArtistService.getAlbumByArtist(artistId, page);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 아티스트별 음원 조회
    @GetMapping(value = "/get/song/{artistId}")
    public ResponseEntity<?> getArtistSong(@PathVariable(name = "artistId") Long artistId,
                                           @RequestParam(name = "page", defaultValue = "1") int page,
                                           HttpServletRequest request) {
        List<SongGetVo> result = iArtistService.getSongByArtist(artistId, page, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping(value = "/update/{artistId}/{artistThumbUri}")
    public ResponseEntity<String> updateArtist(@PathVariable("artistId") Long artistId,
                                               @PathVariable("artistThumbUri") String artistThumbUri) {
        boolean result = iArtistService.updateArtist(artistId, artistThumbUri);

        if (result)
            return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
        else
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("수정 실패");
    }

    @DeleteMapping(value = "/delete/{artistId}")
    public ResponseEntity<String> deleteArtist(@PathVariable("artistId") Long artistId) {
        boolean result = iArtistService.deleteArtist(artistId);

        if (result)
            return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
        else
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("삭제 실패");
    }

}
