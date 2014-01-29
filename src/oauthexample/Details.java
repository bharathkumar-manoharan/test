package oauthexample;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public   class Details {

    @Persistent
    private String firstname;

    @Persistent
    private String name;
    
    @Persistent
    private String username;
      
    @PrimaryKey
    @Persistent
    private String email;
    
    @Persistent
	private String keyblob;
    
    public void setKeyblob(String keyblob) {
		this.keyblob = keyblob;
	}
    
    public String getKeyblob() {
		return keyblob;
	}
    
    @Persistent
    private String gender;
    
/*    @Persistent
    private String country;*/

    public Details() {}



    public String getFirstname() {
        return firstname;
    }
    
    
    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }


    
    public String getGender() {
        return gender;
    }
    
 /*   public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country=country;
    }*/
    
    public void setFirstname(String firstname) {
        this.firstname=firstname;
    }
    
    public void setUsername(String username) {
        this.username=username;
    }
    
    
    public void setName(String name) {
        this.name=name;
    }
    
    public void setEmail(String email) {
        this.email=email;
    }
    
    public void setGender(String gender) {
        this.gender=gender;
    }
    
   
}
