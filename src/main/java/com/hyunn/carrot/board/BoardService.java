package com.hyunn.carrot.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public void write(Board board, MultipartFile file) throws Exception {
        // 파일 저장 경로
        String uploadDir = "files";

        // 랜덤 식별자_원래 파일 이름
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 파일 저장 경로 가져오기
        String projectPath = resourceLoader.getResource("classpath:static").getFile().getAbsolutePath();
        String saveFilePath = projectPath + "/" + uploadDir;

        // 파일 저장 디렉토리 생성
        File uploadPath = new File(saveFilePath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // 파일 저장
        File saveFile = new File(uploadPath, fileName);
        file.transferTo(saveFile);

        if (file.isEmpty()) {
            board.setFilename(null);
            board.setUrl(null);
        } else {
            // Entity에 이름과 경로 저장 -> DB에 저장됨
            board.setFilename(fileName);
            board.setUrl("/" + uploadDir + "/" + fileName);
        }

        boardRepository.save(board); // entity 저장
    }


    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable); // findAll = List<Board> 반환
    }

    // 특정 게시글 불러오기
    public Board boardView(Long id) {
        return boardRepository.findById(id).get(); // id를 받아서 id에 해당하는 게시글을 찾는다.
    }

    // 특정 게시글 삭제
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

}
