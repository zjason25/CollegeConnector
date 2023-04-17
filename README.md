## Project 1 - college recommendation website
### Domain

As opposed to the default Fablix domain, we decided to implement a college recommendation website, where users can view colleges based on rating, SAT scores, or number of votes.

### Database
We used MySQL as our backend database. Our database consists of 9 tables:
- School
- Location
- Genre (types of school)
- Schools_in_locations
- Genre_in_school
- Users
- Recommendation
- Preference
- Ratings

The data we used to populate the database consists of data crawled from the web and artificially generated data. For instance, you will notice some random name college, such as "Consciousness University" or "Hovel College" with randomly genereated SAT scores and LivingCost, all paired with a lively haha yes hedgehog picture.

### Web server
We are using Tomcat10 to host our server and an EC2 t2_micro AWS Ubuntu instance to deploy the application.


### Demo Link
Link to the [demo](https://youtu.be/RS0CCIPbIco)

### Contribution
[Yanran Wang](https://github.com/yanranw1) was responsible for writing the frontend display and the java servelets, also crawling the web for college data.
[Jason Zheng](https://github.com/zjason25) generated artificial data and finalized schemas for the MySQL database, deployed the application on the instance and completed the demo and this README.

Our TA [Xinyuan](https://github.com/aglinxinyuan) and [Yicong](https://github.com/Yicong-Huang) for providing the *cs122b-project1-api-example* from which we can learn build our api for this project.