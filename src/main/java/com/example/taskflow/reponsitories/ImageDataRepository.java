package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.ImageData;
import com.example.taskflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Integer> {
    public List<ImageData> findByUser(User user);
    @Transactional
    default void updateImageData(int id, byte[] newImageData) {
        ImageData imageData = findById(id).orElse(null);
        if (imageData != null) {
            imageData.setImageData(newImageData);
            save(imageData); // Hoặc có thể sử dụng saveAndFlush(imageData);
        }
    }
    @Transactional
    default void saveNewImageData(User user, byte[] newImageData) {
        ImageData imageData = new ImageData();
        imageData.setImageData(newImageData);
        imageData.setUser(user);
        imageData.setName("aaaa");
        imageData.setType("aaaa");
        save(imageData);
    }
}
