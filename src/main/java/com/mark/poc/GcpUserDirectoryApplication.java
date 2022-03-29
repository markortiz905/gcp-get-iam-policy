package com.mark.poc;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudresourcemanager.v3.CloudResourceManager;
import com.google.api.services.cloudresourcemanager.v3.model.Binding;
import com.google.api.services.cloudresourcemanager.v3.model.GetIamPolicyRequest;
import com.google.api.services.cloudresourcemanager.v3.model.Policy;
import com.google.api.services.iam.v1.IamScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * this poc lets you query all users/service accounts from projects specified
 *
 * the service account must have access to bq with the following roles
 * - roles/owner (note - this can be an exaggerating role, use with care.)
 *
 * @author Mark Ortiz
 */
@SpringBootApplication
public class GcpUserDirectoryApplication {

	private static Logger logger = LoggerFactory.getLogger(GcpUserDirectoryApplication.class);

	public static void main(String[] args) {
		// TODO: Replace with your project ID.
		String projectId = "projects/psyched-elixir-345100";

		// Initializes the Cloud Resource Manager service.
		CloudResourceManager crmService = null;
		try {
			crmService = initializeService();
		} catch (IOException | GeneralSecurityException e) {
			logger.error("Unable to initialize service: \n" + e.getMessage() + e.getStackTrace());
		}

		// Get the project's policy and print all members and its roles
		Policy policy = getPolicy(crmService, projectId);
		List<Binding> bindings = policy.getBindings();
		for (Binding b : bindings) {
			logger.info("Role: " + b.getRole());
			for (String m : b.getMembers()) {
				logger.info("\t- " + m + "");
			}
			logger.info("-------------------");
		}

	}

	public static CloudResourceManager initializeService()
			throws IOException, GeneralSecurityException {
		// Use the Application Default Credentials strategy for authentication. For more info, see:
		// https://cloud.google.com/docs/authentication/production#finding_credentials_automatically
		GoogleCredentials credential =
				GoogleCredentials.getApplicationDefault()
						.createScoped(Collections.singleton(IamScopes.CLOUD_PLATFORM));

		// Creates the Cloud Resource Manager service object.
		CloudResourceManager service =
				new CloudResourceManager.Builder(
						GoogleNetHttpTransport.newTrustedTransport(),
						JacksonFactory.getDefaultInstance(),
						new HttpCredentialsAdapter(credential))
						.setApplicationName("iam-quickstart")
						.build();
		return service;
	}

	public static Policy getPolicy(CloudResourceManager crmService, String projectId) {
		// Gets the project's policy by calling the
		// Cloud Resource Manager Projects API.
		Policy policy = null;
		try {
			GetIamPolicyRequest request = new GetIamPolicyRequest();
			policy = crmService.projects().getIamPolicy(projectId, request).execute();
		} catch (IOException e) {
			logger.error("Unable to get policy: \n" + e.getMessage() + e.getStackTrace());
		}
		return policy;
	}

}
