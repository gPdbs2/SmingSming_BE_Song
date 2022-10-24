package com.smingsming.song.entity.playlist.service;

import com.smingsming.song.entity.playlist.entity.PlaylistEntity;
import com.smingsming.song.entity.playlist.entity.PlaylistLikesEntity;
import com.smingsming.song.entity.playlist.repository.IPlaylistLikesRepository;
import com.smingsming.song.entity.playlist.repository.IPlaylistRepository;
import com.smingsming.song.entity.playlist.vo.PlaylistLikesDeleteReqVo;
import com.smingsming.song.entity.playlist.vo.PlaylistLikesResVo;
import com.smingsming.song.global.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistLikesServiceImpl implements IPlaylistLikesService{

    private final IPlaylistLikesRepository iPlaylistLikesRepository;
    private final IPlaylistRepository iPlaylistRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // 플레이리스트 좋아요 추가, 한 번 더 실행 시 취소
    @Override
    public String addPlaylistLikes(Long  playlistId, HttpServletRequest request) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(request)));

        PlaylistEntity playlistEntity = iPlaylistRepository.findById(playlistId).orElseThrow();
        PlaylistLikesEntity playlistLikes = iPlaylistLikesRepository.findByUserIdAndPlaylistEntityId(userId, playlistEntity.getId());

        if (playlistLikes == null) {
            PlaylistLikesEntity addLikes = PlaylistLikesEntity.builder()
                    .userId(userId)
                    .playlistEntity(playlistEntity).build();

            iPlaylistLikesRepository.save(addLikes);
            return "좋아요 성공";
        }

        else {
            iPlaylistLikesRepository.delete(playlistLikes);
            return "좋아요 취소";
        }
    }


    // 좋아요한 플레이리스트 조회
    @Override
    public List<PlaylistLikesResVo> getPlaylistLikes(Long userId) {
        Iterable<PlaylistLikesEntity> playlistLikes = iPlaylistLikesRepository.findAllByUserId(userId);

        List<PlaylistLikesResVo> result = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();

        playlistLikes.forEach(v -> {
            result.add(mapper.map(v, PlaylistLikesResVo.class));
        });

        return result;

    }

    // 플레이리스트 좋아요 취소
    @Override
    public boolean deletePlaylistLikes(PlaylistLikesDeleteReqVo playlistLikesDeleteReqVo) {
        Optional<PlaylistLikesEntity> likes = iPlaylistLikesRepository.findById(playlistLikesDeleteReqVo.getId());

        if(likes.isPresent() && likes.get().getUserId().equals(playlistLikesDeleteReqVo.getUserId())) {
            iPlaylistLikesRepository.deleteById(playlistLikesDeleteReqVo.getId());
            return true;
        }

        return false;
    }
}
