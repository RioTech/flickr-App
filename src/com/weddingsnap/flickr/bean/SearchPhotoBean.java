/**
 * 
 */
package com.weddingsnap.flickr.bean;

import java.io.Serializable;

/**
 * This bean will hold all the information  about public photo
 * @author Ravi bhojani
 *
 */
@SuppressWarnings("serial")
public class SearchPhotoBean implements Serializable{

	private long id;
	private String owner;
	private String secret;
	private long server;
	private int farm;
	private String title;
	private boolean isPublic;
	private boolean isFriend;
	private boolean isFamily;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public long getServer() {
		return server;
	}
	public void setServer(long server) {
		this.server = server;
	}
	public int getFarm() {
		return farm;
	}
	public void setFarm(int farm) {
		this.farm = farm;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public boolean isFriend() {
		return isFriend;
	}
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	public boolean isFamily() {
		return isFamily;
	}
	public void setFamily(boolean isFamily) {
		this.isFamily = isFamily;
	}
}
