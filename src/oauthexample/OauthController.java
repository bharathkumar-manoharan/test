package oauthexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.face4j.facebook.Client;
import com.face4j.facebook.Facebook;
import com.face4j.facebook.OAuthAccessToken;
import com.face4j.facebook.entity.User;
import com.face4j.facebook.enums.Display;
import com.face4j.facebook.enums.HttpClientType;
import com.face4j.facebook.enums.Permission;
import com.face4j.facebook.exception.FacebookException;
import com.face4j.facebook.factory.FacebookFactory;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.labs.repackaged.org.json.JSONException;
@Controller
public class OauthController {
	User user = null;
	Facebook facebook;
	String firstname,email,name,gender,profileImage,username;
	@RequestMapping(value = "/",  method = RequestMethod.GET)
	protected String loginPage(HttpServletRequest request,HttpServletResponse response)
	{
		return "index";
	}

	@RequestMapping(value = "/facebookOauthCallback",  method = RequestMethod.GET)
	protected void facebooksignup(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException
	{
		System.out.println("id is here ::: "+ request.getAttribute("id"));
		request.getSession();
		Client client = new Client("135771289896004", "860e48f07a0fbc6b98b24bfa3b556f44");
		FacebookFactory facebookFactory = new FacebookFactory(client,HttpClientType.URL_FETCH_SERVICE);
		String code = request.getParameter("code");
		

		if(code == null)
		{
			String redirectURL = facebookFactory.getRedirectURL("http://localhost:8888/facebookOauthCallback", Display.POPUP, Permission.EMAIL, Permission.OFFLINE_ACCESS,Permission.PUBLISH_STREAM);
			try 
			{
				response.sendRedirect(redirectURL);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			OAuthAccessToken accessToken = null;
			try 
			{
				accessToken = facebookFactory.getOAuthAccessToken(code, "http://localhost:8888/facebookOauthCallback");

				facebook = facebookFactory.getInstance(accessToken);
				user = facebook.getCurrentUser();
			} 
			catch (FacebookException e) 
			{
				e.printStackTrace();
			}
		email=user.getLink().replace("http://www.facebook.com/","")+"@facebook.com";
		name=user.getName();
		firstname=user.getFirstName();
		gender=user.getGender();
		profileImage=user.getPictureURL()+"?type=large";
		username = user.getUsername();
		String urladdr = "https://graph.facebook.com/me/feed?access_token="+accessToken.getAccessToken()+"&method=POST&name=People%20Argue%20Just%20to%20Win&link=http://www.nytimes.com/2011/06/15/arts/people-argue-just-to-win-scholars-assert.html";
		   URL url = new URL(urladdr);
		      String inputLine;
		      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		      conn.setRequestMethod("POST");
		      conn.connect();
		      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		      
		      while ((inputLine = reader.readLine()) != null)
				{		
		    	  System.out.println("inputLine ::"+inputLine);
				}
		}
	}
	
	@RequestMapping(value = "/usercheck",  method = RequestMethod.GET)
	protected String userCheck(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException, FacebookException
	{
		PersistenceManager pmf = Persist.get().getPersistenceManager();
		Details detailsObject = new Details();

		Query query = pmf.newQuery(Details.class,"email == emailid");
		query.declareParameters("String emailid");
		
		List<Details> signin = (List<Details>) query.execute(email);
		
		if(signin.isEmpty() || signin == null){
			detailsObject.setEmail(email);
			//detailsObject.setCountry(user.getLocation().getCountry());
			detailsObject.setFirstname(firstname);
			detailsObject.setName(name);
			detailsObject.setGender(gender);
			detailsObject.setUsername(username);
			pmf.makePersistent(detailsObject);
			pmf.close();
			session.setAttribute("firstname",firstname);
			session.setAttribute("name",name);
			session.setAttribute("gender",gender);
			session.setAttribute("imageurl", profileImage);
			session.setAttribute("email", email);
		}else{
		//session.setAttribute("country",user.getHometown().getName());
		session.setAttribute("firstname",firstname);
		session.setAttribute("name",name);
		session.setAttribute("gender",gender);
		session.setAttribute("imageurl", profileImage);
		session.setAttribute("email", email);
		}
//		response.sendRedirect("https://www.facebook.com/dialog/feed?app_id=135771289896004&link=https://developers.facebook.com/docs/reference/dialogs/&picture=http://fbrell.com/f8.jpg&name=Facebook%20Dialogs&caption=Reference%20Documentation&description=Using%20Dialogs%20to%20interact%20with%20users.&redirect_uri=http://localhost:8888/facebookOauthCallback");
		return "profile";
	}
	@RequestMapping(value = "/googleOauthCallback")
	protected void googleoauth(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException, JSONException
	{
		String code = request.getParameter("code");
		System.out.println("code is outside"+code);
		if(code == null){
			
			String redirectUrl="https://accounts.google.com/o/oauth2/auth?"+
					"scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&"+
					"state=%2Fprofile&redirect_uri=http://localhost:8888/googleOauthCallback&"+
					"response_type=code&client_id=493085814903.apps.googleusercontent.com&approval_prompt=force";
			try {
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
		System.out.println("code is here"+code);
		TokenResponse accessresponse = new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),new GenericUrl("https://accounts.google.com/o/oauth2/token"), code).setRedirectUri("http://localhost:8888/googleOauthCallback").setClientAuthentication(new ClientParametersAuthentication("493085814903.apps.googleusercontent.com", "O2CLw3z258EZs4mIdXy7JQqg")).execute();
		String accesstoken=accessresponse.getAccessToken();
		System.out.println("Access token1: " + accesstoken);
		
		String googleapiaddress = "https://www.googleapis.com/oauth2/v1/userinfo?access_token="+accesstoken+"";
		GenericUrl userurl = new GenericUrl(googleapiaddress);
		Credential usercredentials=new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(accessresponse);
		HttpRequestFactory userrequest = new NetHttpTransport().createRequestFactory(usercredentials);
		HttpRequest req = userrequest.buildGetRequest(userurl);
		HttpResponse userDetailsUrl = req.execute();
		BufferedReader userDetails = new BufferedReader(new InputStreamReader(userDetailsUrl.getContent()));
		
		
		
		
		String parsingString="";
		 System.out.println("Google user details");
		    for (String line = userDetails.readLine(); line != null; line = userDetails.readLine()) {
		      System.out.println(line);
		      parsingString+=line;
		    }
		    ObjectMapper mapper = new ObjectMapper();
		     mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		      if(parsingString != null && !("".equalsIgnoreCase(parsingString))){
		    	  JsonNode jnode=mapper.readValue(parsingString,JsonNode.class);
		    	  email = jnode.get("email").toString().replaceAll("\"", "");
		    	  if(jnode.get("given_name") != null)
		    		  firstname = jnode.get("given_name").toString().replaceAll("\"", "");
		    	  if(jnode.get("name") != null)
		    		  name = jnode.get("name").toString().replaceAll("\"", "");
		    	  if(jnode.get("picture") != null)
		    		  profileImage =  jnode.get("picture").toString().replaceAll("\"", ""); 
		    	  if(jnode.get("picture") != null)
		    		  gender=  jnode.get("gender").toString().replaceAll("\"", "");
		    	  username=email.substring(0,email.indexOf("@"));
		      }
		      
				System.out.println("username : "+username);
		      response.sendRedirect("/usercheck");
		}
	}
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	@RequestMapping(value = "/upload",  method = RequestMethod.POST)
	protected void upload(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException
	{
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		
		ImagesService imagesService 				= ImagesServiceFactory.getImagesService();
		
		List<BlobKey> blobsList		= blobs.get("myFile");
		
		BlobKey blobKey				= null;
		
		for(BlobKey newblobKey : blobsList )
		{
			if(newblobKey != null)
			{
				blobKey = newblobKey;
			}
		}
		
		String imageUrl = imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey));
		
		
		System.out.println("blobkey :: "+blobKey);
		System.out.println("image url :: "+imageUrl);
		
		session.setAttribute("imageurl", imageUrl);
		
		response.sendRedirect("profile.jsp");
        
//        blobstoreService.serve(blobKey, response);
	}
}
