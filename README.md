# GCP Project Users/Service Accounts List API POC
This POC demonstrates how to list down users and service accounts from specified project from GCP.

# Getting Started
  The process is simple, just create a serviceaccount with right permission(you can assigne role/owner) to perform listing down users from projects
  and make sure to create json key and assign to GOOGLE_APPLICATION_CREDENTIALS env variable.

## How to Run? This part will be consuming the reports from BigQuery
- Make sure you set the envoronment variable `GOOGLE_APPLICATION_CREDENTIALS=your-service-account-key.json`
- `./gradlew bootRun` from root project. 
- it will print basic total cost for each month used. 

## References
- https://cloud.google.com/iam/docs/write-policy-client-libraries


# Support?
you can reach out to the git repo owner or email me at ortizmark905@gmail.com, you can also look for me from linked in and upwork.





