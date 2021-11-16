package com.psl.miniProject.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ratings")
public class Ratings {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="product_id")
	private int product_id;
	@Column(name="five_star",columnDefinition = "integer default 0")
	private int five_star;
	@Column(name="four_star",columnDefinition = "integer default 0")
	private int four_star;
	@Column(name="three_star",columnDefinition = "integer default 0")
	private int three_star;
	@Column(name="two_star",columnDefinition = "integer default 0")
	private int two_star;
	@Column(name="one_star",columnDefinition = "integer default 0")
	private int one_star;
	
	public Ratings( int five_star, int four_star, int three_star, int two_star, int one_star) {
		this.five_star = five_star;
		this.four_star = four_star;
		this.three_star = three_star;
		this.two_star = two_star;
		this.one_star = one_star;
	}
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getFive_star() {
		return five_star;
	}
	public void setFive_star(int five_star) {
		this.five_star = five_star;
	}
	public int getFour_star() {
		return four_star;
	}
	public void setFour_star(int four_star) {
		this.four_star = four_star;
	}
	public int getThree_star() {
		return three_star;
	}
	public void setThree_star(int three_star) {
		this.three_star = three_star;
	}
	public int getTwo_star() {
		return two_star;
	}
	public void setTwo_star(int two_star) {
		this.two_star = two_star;
	}
	public int getOne_star() {
		return one_star;
	}
	public void setOne_star(int one_star) {
		this.one_star = one_star;
	}
	
	public Ratings() {
		super();
	}
	
}
