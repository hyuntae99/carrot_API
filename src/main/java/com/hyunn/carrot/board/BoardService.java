package com.hyunn.carrot.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ResourceLoader resourceLoader;
    private final AmazonS3 amazonS3;
    private final AWSService awsService;
    private final String bucketName;

    @Autowired
    public BoardService(BoardRepository boardRepository, ResourceLoader resourceLoader, AmazonS3 amazonS3, AWSService awsService, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.boardRepository = boardRepository;
        this.resourceLoader = resourceLoader;
        this.amazonS3 = amazonS3;
        this.awsService = awsService;
        this.bucketName = bucketName;
    }

    public void write(Board board, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            String fileUrl = uploadFile(file);
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            board.setFilename(fileName);
            board.setUrl(fileUrl);
        } else {
            board.setUrl(null);
        }
        boardRepository.save(board);
    }

    public void update(Board board, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            String fileUrl = uploadFile(file);
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            board.setFilename(fileName);
            board.setUrl(fileUrl);
        }
        boardRepository.save(board);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String uploadDir = "files";
        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        byte[] fileBytes = file.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        amazonS3.putObject(new PutObjectRequest(bucketName, uploadDir + "/" + fileName, file.getInputStream(), metadata));
        return awsService.getFileUrl(bucketName, uploadDir + "/" + fileName);
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Board boardView(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
