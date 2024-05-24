package org.example.lionminiproj2.controller;

import lombok.RequiredArgsConstructor;
import org.example.lionminiproj2.domain.Board;
import org.example.lionminiproj2.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    @GetMapping("/")
    public String redirectToList() {
        return "redirect:/list";
    }

    @GetMapping("/list")
    // 1. 게시글 조회
    public String ListHtml(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page -1, size);
        Page<Board> boards = service.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);

        return "boards/list";
    }


    // 2. 게시글 상세 조회
    @GetMapping("/view/{id}")
    public String viewHtml(@PathVariable Long id, Model model) {
        Board board = service.findBoardById(id);
        model.addAttribute("board", board);
        return "boards/view";
    }


    // 3. 신규 게시글 등록
    @GetMapping("/writeform")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "boards/writeform";
    }
    @PostMapping("/write")
    public String writePost(@ModelAttribute("board") Board board) {
        service.saveBoard(board);
        return "redirect:/list";
    }


    // 4. 기존 게시글 삭제
    @GetMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("boardId", id);
        return "boards/deleteform";
    }
    @PostMapping("/delete")
    public String deletePost(@RequestParam Long id, @RequestParam String password) {
        if (service.deleteBoard(id, password)) {
            return "redirect:/list";
        } else {
            // 암호가 올바르지 않은 경우에 대한 처리
            return "redirect:/view/" + id;
        }
    }


    // 5. 기존 게시글 수정
    @GetMapping("/updateform/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", service.findBoardById(id));
        return "boards/updateform";
    }
    @PostMapping("/update")
    public String editPost(@ModelAttribute Board board) {
        service.updateBoard(board.getId(), board, board.getPassword());
        return "redirect:/view/" + board.getId();
    }
}
