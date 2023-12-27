package com.ravismishra99.BatchApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String description;
	private String image1;
	private String image2;
	private String image3;
	private String createdAt;
	private String updatedAt;
	private String category_id;
	private String category_name;
	private String category_image;
	private String category_createdAt;
	private String category_updatedAt;

}
