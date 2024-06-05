package com.ecommerce.app.repository;

import com.ecommerce.app.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ContactRepo extends JpaRepository<Contact,Long> {
}
