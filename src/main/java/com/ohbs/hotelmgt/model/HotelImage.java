package com.ohbs.hotelmgt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotel_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
//	@ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
//	@JoinColumn(name = "hotel_id")
	private Hotel hotel;
}
