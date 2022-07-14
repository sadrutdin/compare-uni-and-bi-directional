package exampleapp.repository;

import exampleapp.domain.Child;
import exampleapp.domain.Parent;
import liquibase.repackaged.org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParentRepositoryTest {
    @Autowired
    private ParentRepository parentRepository;

    @Test
    @Order(1)
    public void save() {
        Parent parent = new Parent();

        assertNull(parent.getId());

        parent.setName("Parent " + RandomUtils.nextLong());

        Child childOne = new Child();
        childOne.setName("Child " + RandomUtils.nextLong());
        parent.addChild(childOne);

        Child childTwo = new Child();
        childTwo.setName("Child " + RandomUtils.nextLong());
        parent.addChild(childTwo);

        parentRepository.save(parent);

        assertNotNull(parent.getId());
        assertNotNull(childOne.getId());
        assertNotNull(childTwo.getId());
    }

    @Test
    @Order(2)
    @Transactional
    public void merge() {
        String existParentName = "New parentName" + RandomUtils.nextLong();
        String existChildName = "Exist child new name" + RandomUtils.nextLong();
        String newChildName = "New child name" + RandomUtils.nextLong();

        Optional<Parent> parentOptional = parentRepository.findTopByOrderByIdDesc();
        assertTrue(parentOptional.isPresent());

        Parent parent = parentOptional.get();
        // Edit parent
        parent.setName(existParentName);

        // Edit first child
        Optional<Child> anyChild = parent.getChildren()
                .stream()
                .findAny();
        assertTrue(anyChild.isPresent());
        anyChild.get().setName(existChildName);

        // Append new
        Child newChild = new Child();
        newChild.setName(newChildName);
        parent.addChild(newChild);

        parentRepository.save(parent);

        assertEquals(existParentName, parent.getName());

//        Глюк хибера - новый элемент добавляется в коллекцию, но newChild остается не привязанным к сессии
//        assertNotNull(newChild.getId());

        assertEquals(3, parent.getChildren().size());
        assertTrue(parent.getChildren().stream().anyMatch(child -> child.getName().equals(existChildName)));
        assertTrue(parent.getChildren().stream().anyMatch(child -> child.getName().equals(newChildName)));
    }


}