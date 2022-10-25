package com.smingsming.song.entity.album.controller;

import com.smingsming.song.entity.album.entity.AlbumEntity;
import com.smingsming.song.entity.album.vo.AlbumAddRequestVo;
import com.smingsming.song.entity.album.service.IAlbumService;
import com.smingsming.song.entity.album.vo.AlbumDetailVo;
import com.smingsming.song.entity.album.vo.AlbumVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
public class AlbumController {
    private final IAlbumService iAlbumService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addAlbum(@RequestBody AlbumAddRequestVo albumVo) {
        AlbumEntity result = iAlbumService.addAlbum(albumVo);

        if(result != null)
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping(value = "/get/{albumId}")
    public ResponseEntity<?> getAlbum(@PathVariable(value = "albumId") Long albumId) {
        AlbumEntity result = iAlbumService.getAlbum(albumId);

        if(result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("없는 앨범ID 입니다.");
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchAlbum(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                         @RequestParam(name = "page", defaultValue = "1") int page) {
        List<AlbumVo> result = iAlbumService.albumSearch(keyword, page);

        if(result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("없는 앨범ID 입니다.");
    }

    @GetMapping(value = "/get/detail/{albumId}")
    public ResponseEntity<?> getAlbumDetail(@PathVariable(name = "albumId") Long albumId,
                                            HttpServletRequest request) {
        AlbumDetailVo result = iAlbumService.getAlbumDetail(albumId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping(value = "/delete/{albumId}")
    public ResponseEntity<?> deleteAlbum(@PathVariable(value = "albumId") Long albumId) {
        boolean result = iAlbumService.deleteAlbum(albumId);
        
        if(result)
            return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제 실패");
    }

}
