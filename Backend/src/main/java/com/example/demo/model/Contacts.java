package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "contacts")
@Table(name = "Contacts")
public class Contacts {

    @Valid

    @Id
    @SequenceGenerator(
            name = "contacts_seq",
            sequenceName = "contacts_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "contacts_seq"
    )

    @Column(
            name = "contactId"
    )
    private Long contactId;


    @Column(
            name = "email",
            unique = true
    )
    @Email(message = "Invalid email number format")
    private String email;

    @Column(
            name = "phone",
            unique = true
    )
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number format")
    private String phone;


    public Contacts(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public Contacts(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "Contacts{" +
                "contactId=" + contactId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
