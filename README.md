## College recommendation website
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

## [Project 2 Addition]: 
### Login
In project 2, we added a sign-in page, a feature that prevents users from accessing any other pages unless signed in. 

### Search/Browse
We implemented substring matching in our keyword search by using the LIKE operator in MySQL. Additionally, the website also now supports browsing colleges by their initial letters and type. For instance, a drop-down feature allows the user to select "Public University" and initiate search from there.

### Wishlist(Shopping Cart) and Recommendation(Checkout)
Similar to a shopping cart in the Fablix domain, our college recommendation website CollegeConnector(a placeholder name) uses a wishlist to allows the users to choose a college of their own liking. Inside the wishlist, the user has access to the colleges' info such as name, location, and state. The user can add and delete college inside the wishlist. From the wishlist, the user may proceed to a Calculate Match page, where the user enters personal statistics such as SAT scores, range of tuition cost, and school genre, from which the reader can click on "calculate match", which ultimately directs the reader to a page showing a best-matching college from the user's wishlist.

### How's our domain different from Fablix
We did not include a "sale date" attribute in our schema for the recommendation table, which is equivalent to the sales table in Fablix. Hence, the demonstration only shows a increase in the number of records as proof for successful insert into our recommendation table.

### Link to our [demo](https://youtu.be/7Z1LOKpo5rY) for Project 2

To test our site, use login:
SuzanneAshley16@yahoo.gov and password:
*FOT@!aZy60z
at [link](http://54.67.47.84:8080/cs122b-project2-login-cart-example/login.html)


### Contribution
Huge shout-out to [Yanran](https://github.com/yanranw1) for her tireless efforts in implementing the login and search/browse, and checkout services. [Jason](https://github.com/zjason25) was responsible for writing services related to Wishlist and styling the login and checkout page.

Once again, our TA Xinyuan and Yicong for provided the example codes on session/form and login page from which we can learn build our api for this project.



## Project 3 Addition:


Report inconsistency data: Report inconsistency data to the user and included in README: We inserted all the information from XML. For the celebrity info for each school. We created a new table celebrities to store the celebrity information and use a new table query_celebrities_in_schools to store the relationship between two celebrities and schools. We ignored all repeated information.

## Two optimization strategies:
Our runtime mostly came down to DOM tree construction and SQL insertion. For the former, we opted to disabling XML validation and ExpandEntityReference as to reduce the overhead and additional processing with DOM tree construction. As for our SQL insertion, simply turning off auto-commit was not enough of an improvement. As such, we decided to use one single insert statement to insert multiple values. This measure alone improved our insertion time from minutes to a matter of just a few seconds.

### Prepared Statements:
We used prepared statements in the following files:
- AddGenre
- AddLocation
- AddSchool
- LoginServlet
- SearchServlet
- SingleLocationServlet
- SingleSchoolSerlvet
- EmployeeLoginServlet

### NewFileName Explanation
File LocationParser is our dom parser for location xml which contains information about locations.
File SchoolParser is our dom parser for school xml which contains information about schools.
File SchoolMap is our dom parser for location_school xml which contains information about location_school relationship.

### Custom Domain Further Explanation
We have 12000 school info in our school xml file. The total file size is about 20MB. Besides regular school data we also added a new data, celebrity alumni for each school as sub-directory. 
We have 6800 location info in our location xml file

### Our new domain name is:
collegecompasss.com (with 3 "s").

### Link to our [demo](https://youtu.be/xGNBrtFa-HI)

### Contribution
Yanran Wang worked on TASK 3, 4, 5, and 6. She encrypted password, implemented a Dashboard using Stored Procedure, generated our three xml files for our custom domain, wrote parsers for all three xml files and helped with optimization.

Jason Zheng worked on Task 1 to 3 and Domain registration. He also helped with parser optimization, project deployment, and demo recording.

TA [Xinyuan](https://github.com/aglinxinyuan) and [Yicong](https://github.com/Yicong-Huang) provided examples on [reCaptcha](https://github.com/UCI-Chenli-teaching/cs122b-project3-recaptcha-example), [encryption](https://github.com/UCI-Chenli-teaching/cs122b-project3-encryption-example), and [DOM parser](https://github.com/UCI-Chenli-teaching/cs122b-project3-DomParser-example).
