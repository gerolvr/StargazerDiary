package com.gerolivo.stargazerdiary.astrodata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AstroObjectData {

    private Target target;
    private RightAsention ra;
    private Dec dec;
    private Image image;
    private Category category;
    

    public AstroObjectData() {
    }

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public RightAsention getRa() {
		return ra;
	}

	public void setRa(RightAsention rightAscension) {
		this.ra = rightAscension;
	}

	public Dec getDec() {
		return dec;
	}

	public void setDec(Dec dec) {
		this.dec = dec;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "SkyObjectValue [target=" + target + ", rightAscension=" + ra + ", dec="
				+ dec + ", image=" + image + ", category=" + category + "]";
	}


}
