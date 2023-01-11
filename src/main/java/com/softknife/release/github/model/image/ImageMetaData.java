package com.softknife.release.github.model.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
public class ImageMetaData{

	@JsonProperty("image")
	private Image image;

}