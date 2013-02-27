/**
 * 
 */
package com.weddingsnap.flickr.bean;

/**
 * This bean hold all the data related to comment on public photos
 * @author Ravi Bhojani
 *
 */
public class CommentBean {

	private String autherName;
	private String comment;
	
	public String getAutherName() {
		return autherName;
	}
	public void setAutherName(String autherName) {
		this.autherName = autherName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
