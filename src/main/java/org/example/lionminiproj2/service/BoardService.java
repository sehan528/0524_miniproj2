package org.example.lionminiproj2.service;

import lombok.RequiredArgsConstructor;
import org.example.lionminiproj2.domain.Board;
import org.example.lionminiproj2.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;

    // 조회
    @Transactional
    public Iterable<Board> findAllBoards() {
        return repository.findAll();
    }
    // 페이징
    public Page<Board> findAllBoards(Pageable pageable) {
        Pageable sortedByDescID = PageRequest.of(pageable.getPageNumber() , pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(sortedByDescID);
    }

    // 게시글 상세 조회
    @Transactional
    public Board findBoardById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // 신규 게시글 등록
    @Transactional
    public Board createBoard(Board board) {
        return repository.save(board);
    }

    // 기존 게시글 삭제
    @Transactional
    public boolean deleteBoard(Long id, String password) {
        Board board = repository.findById(id).orElse(null);
        if (board == null) {
            return false;
        }

        if (!password.equals(board.getPassword())) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    // 기존 게시글 수정
    @Transactional
    public Board saveBoard(Board board) {
        LocalDateTime now = LocalDateTime.now();
        board.setCreatedAt(now);
        board.setUpdatedAt(now);
        return repository.save(board);
    }

    @Transactional
    public Board updateBoard(Long id, Board updatedBoard, String password) {
        Board board = repository.findById(id).orElse(null);
        // 게시글이 없을 경우 -> 패스워드 비교
        if (board == null) {

            return null;
        }

        if (!password.equals(board.getPassword())) {
            return null;
        }

        // 갱신 -> 재설정
        board.setName(updatedBoard.getName());
        board.setTitle(updatedBoard.getTitle());
        board.setContent(updatedBoard.getContent());

        board.setUpdatedAt(LocalDateTime.now());

        return repository.save(board);
    }
}
