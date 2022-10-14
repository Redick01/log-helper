package io.redick.example.controller;

import com.redick.annotation.LogMarker;
import io.redick.example.dao.NameDao;
import io.redick.example.entity.NameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 */
@RestController
public class NameController {

    @Autowired
    private NameDao nameDao;

    @GetMapping("/trace/getName")
    @LogMarker(businessDescription = "getName")
    public NameEntity getName(Integer id) {
        return nameDao.selectById(id);
    }
}
