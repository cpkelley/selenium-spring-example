package com.dealer.test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;


public class ModelLineupPage extends CmsPage {
	
	private static final By min_price_slider = By.cssSelector(".ui-slider-value .min-price"); 
	private static final By max_price_slider = By.cssSelector(".ui-slider-value .max-price");
	protected By filtered_vehicle_item = By.cssSelector(".hproduct[style*=\"opacity: 1\"]");
	private String page = "/showroom/index.htm";
	
	@FindBy (css = ".hproduct") 
	private List<WebElement> listings;
	
	public ModelLineupPage() {
		super();
		this.setPage(page);
	}
	
	public ModelLineupPage(SeleniumManager sm) {
		super(sm);
		this.setPage(page);
	}
	
	public String getMinPriceLabel() {
		return findElement(min_price_slider).getText();
	}
	
	public String getMaxPriceLabel() {
		return findElement(max_price_slider).getText();
		
	}
	
	public boolean hasFilteredVehicles() {
		return this.isElementPresentAndDisplayed(filtered_vehicle_item, 6000);
		
	}
	
	
	public List<VehicleListing> getDisplayedVehiclesList() {
		ArrayList<VehicleListing> dvl = new ArrayList<VehicleListing>();	
		for (WebElement model : listings){
			dvl.add((ShowroomVehicleListing) this.getVehicleListing(model));
		}
		return dvl;
	}			

	public VehicleListing getVehicleListing(WebElement current) {
		return new ShowroomVehicleListing(current);
	}
		
	public class ShowroomVehicleListing implements VehicleListing {
		
		private WebElement vehiclelisting;
		private By price = By.cssSelector(".price" );
		private By title = By.cssSelector(".fn.h3 a");
		
		public ShowroomVehicleListing() {
			super();
		}
		
		public ShowroomVehicleListing(WebElement vehiclelisting) {
			this.vehiclelisting = vehiclelisting;
		}
		

		@Override
		public String getPrice() {
			return formatPrice(vehiclelisting.findElement(price).getText());
		}

		@Override
		public String getTitle() {
			return vehiclelisting.findElement(title).getText();
		}

		public String getTrim() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public WebElement getLink() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void click() {
			vehiclelisting.click();
		}

		@Override
		public boolean isCertified() {
			throw new UnsupportedOperationException();
		}

		public Integer getMinHp() {
			return Integer.parseInt(
					vehiclelisting.getAttribute("data-minhorsepower"));
		}

		public Integer getMaxHp() {
			return Integer.parseInt(
					vehiclelisting.getAttribute("data-maxhorsepower"));
		}

		public Integer getMinMpg() {
			return Integer.parseInt(
					vehiclelisting.getAttribute("data-maxfe"));
		}

		public Integer getMaxMpg() {
			return Integer.parseInt(
					vehiclelisting.getAttribute("data-minfe"));
		}

		public Integer getMinPrice() {
			return Integer.parseInt(
					vehiclelisting.getAttribute("data-minprice"));
		}

		public Integer getMaxPrice() {
			return Integer.parseInt(
					vehiclelisting.getAttribute("data-maxprice"));
		}
			
	}

	public boolean clickShowroomVehicle(String modelTitle) {
		Boolean result = false;
		for (VehicleListing model : this.getDisplayedVehiclesList()) {
			if (model.getTitle().contains(modelTitle)) {
				model.click();
				result = true;
				break;
			}
		}
		return result;
		
	}
	
	public boolean hasListings() {
		return listings.size() != 0;
	}

	public Boolean isSliderDisplayed() {
		return this.isElementPresentAndDisplayed(min_price_slider);
	}
}


 
	
