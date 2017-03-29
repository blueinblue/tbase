package org.bib.tbase.web.flow.system.userreg;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.display.Displayable;
import org.bib.tbase.domain.user.UserRegistration;
import org.bib.tbase.service.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

/**
 * New user registration backing form.
 */
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class UserRegForm implements Serializable, UserRegistration {
// Constructors
	public UserRegForm() {
		// Create the email challenge code
		String uuidStr = UUID.randomUUID().toString();
		this.emailChallengeCode = uuidStr.substring(0, uuidStr.indexOf("-"));
	}

// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}
	
	/**
	 * Returns true iff emailChallengeCode equals ignore case emailConfirmationCode.
	 * 
	 * @return A boolean
	 */
	public boolean isEmailCodeMatched() {
		return StringUtils.equalsIgnoreCase(emailChallengeCode, emailConfirmationCode);
	}
	
	/**
	 * Retrieves a message from the system - to showcase ajax modal dialog.
	 * @return
	 */
	public String getSystemMessage() {
		String dateStr = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ss").format(new Date());
		
		return "Welcome to Cyberdyne Systems.  The current date and time is " + dateStr;
	}
	
// Getters & Setters
	@Override
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = StringUtils.trimToEmpty(email);
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = StringUtils.trimToEmpty(displayName);
	}

	@Override
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = StringUtils.trimToEmpty(password);
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = StringUtils.trimToEmpty(passwordConfirm);
	}
	
	@Override
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public List<Interest> getInterests() {
		return interests;
	}
	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}
	
	public Interest getPrimaryInterest() {
		return primaryInterest;
	}
	public void setPrimaryInterest(Interest primaryInterest) {
		this.primaryInterest = primaryInterest;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getEmailConfirmationCode() {
		return emailConfirmationCode;
	}

	public void setEmailConfirmationCode(String emailConfirmationCode) {
		this.emailConfirmationCode = emailConfirmationCode;
	}
	public String getEmailChallengeCode() {
		return emailChallengeCode;
	}
	
// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserRegForm.class);
	
	/**
	 * E-Mail Address - will be used to login to their account
	 */
	@NotBlank(message="Required field", groups=UserEssentials.class)
	private String email;
	
	/**
	 * Display Name - how user will appear to other users, must be unique
	 */
	@NotBlank(message="Required field", groups=UserEssentials.class)
	private String displayName;
	
	/**
	 * Password
	 */
	@NotBlank(message="Required field", groups=UserEssentials.class)
	private String password;
	
	/**
	 * Password confirm (must match password attribute)
	 */
	@NotBlank(message="Required field", groups=UserEssentials.class)
	private String passwordConfirm;
	
	/**
	 * Birthday
	 */
	private Date birthday;
	
	/**
	 * Areas of interest
	 */
	private List<Interest> interests = new LinkedList<Interest>();
	
	/**
	 * Primary interest
	 */
	private Interest primaryInterest = null;
	
	/**
	 * Message to sysop
	 */
	private String message;
	
	/**
	 * E-mail challenge code - sent to the user to verify their email address
	 */
	private final String emailChallengeCode;
	
	/**
	 * E-mail confirmation code - entered by the user to verify their email address - must match the challenge code.
	 */
	private String emailConfirmationCode;
	
// Validation Groups
	/**
	 * Verification group for essential user data - email, display name, password, etc.
	 */
	public interface UserEssentials {
	}
	
	/**
	 * Verification group for email verification fields.
	 */
	public interface EmailVerification {
		
	}
	
// Public Enums
	public enum Interest implements Displayable {
		MOBILE_DEV("Mobile Dev"), EMBED_DEV("Embedded Dev"), WEB_DEV("Web Dev"),
		HARDWARE("Hardware"), SOFTWARE("Software"),
		LINUX("Linux"), WINDOWS("Windows"), WARP("Warp OS");

		// Constructors
		private Interest(String displayName) {
			this.displayName = displayName;
		}
		
		// Getters & Setters
		@Override
		public Object getValue() {
			return this;
		}
	
		@Override
		public String getDisplayName() {
			return displayName;
		}
		
		// Attributes
		private final String displayName;
	}
}
