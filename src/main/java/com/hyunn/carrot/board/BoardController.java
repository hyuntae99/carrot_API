package com.hyunn.carrot.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller // 컨트롤러 어노테이션
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") // 어떤 url로 접근할 것인가
    public String boardWriteForm() {

        return "boardwrite"; // boardwrite html 파일로 넘어감

    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{

        boardService.write(board, file);

        // model에 message와 searchUrl을 담는다.
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        // model.addAttribute("message", "글 작성이 실패하였습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";

    }


    @GetMapping("/board/list")
    public String BoardList(Model model,
                            // page = 0 -> 첫번째, sort = 정렬 기준, size = 한 페이지의 보여지는 갯수, direction = 오름차, 내림차
                            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        Page<Board> list = null;
        if(searchKeyword == null) { // 검색 키워드가 없을 때
            list = boardService.boardList(pageable); // 기존 리스트
        } else { // 검색 키워드가 있을 때
            list = boardService.boardSearchList(searchKeyword, pageable); // 검색 기능 포함 리스트
        }

        // 현재 페이지
        int nowPage = list.getPageable().getPageNumber() + 1; // page는 0에서 시작한다!
        // 블록에서 보여줄 시작 페이지
        int startPage = Math.max(nowPage - 4, 1); // 적어도 한 개의 페이지는 나오게
        // 블록에서 보여줄 마지막 페이지
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 최대 페이지 수 까지만 나오게

        model.addAttribute("list", list); // 리스트를 반환하고 list라는 이름에 담아서 넘기겠다.

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist"; // boardList html 파일로 넘어감

    }


    @GetMapping("/board/view") //localhost:8080/board/view?id=1
    public String BoardView(Model model, Long id) {

        model.addAttribute("board", boardService.boardView(id));
        return "boardview";

    }

    @GetMapping("/board/delete")
    public String BoardDelete(Long id, Model model) {

        boardService.boardDelete(id); // id에 맞는 게시글 삭제 처리 후
        model.addAttribute("message", "글 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
        // return "redirect:/board/list"; // 변경사항 업데이트 후 list 페이지로 이동한다.

    }

    // 수정하기 위한 수정 폼으로 넘어가는 컨트롤러
    @GetMapping("/board/modify/{id}")
    // {}안의 id를 인식해서 Int 형식으로 변환하여 사용가능
    // /숫자 형식
    public String BoardModify(@PathVariable("id") Long id, Model model) {

        model.addAttribute("board", boardService.boardView(id)); // 상세페이지(view)와 같다
        return "boardmodify";

    }


    @PostMapping("/board/update/{id}")
    public String BoardUpdate(@PathVariable("id") Long id, Board board, Model model, MultipartFile file) throws Exception {

        Board boardTemp =boardService.boardView(id); // id에 해당하는 기존 글 불러오기
        boardTemp.setTitle(board.getTitle()); // title 덮어쓰기
        boardTemp.setContent(board.getContent()); // content 덮어쓰기
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            boardTemp.setFilename(fileName);
            boardTemp.setUrl("/files/" + fileName);
        }

        boardService.write(board, file);

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
        // return "redirect:/board/list";

    }
}

