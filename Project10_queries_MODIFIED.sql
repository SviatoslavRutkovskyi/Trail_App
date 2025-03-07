# Changed USER user_id to auto_increment
# Removed user_id from INSERT statements on USER table

CREATE DATABASE Project10_db;

USE Project10_db;

CREATE TABLE TRAIL (
    trail_id				Integer			NOT NULL,
    trail_name				VarChar(20)		NULL,
    trail_length			Decimal(5,1)	NULL,
    trail_difficulty		Integer			NULL,
    trail_rating			Decimal(3,2)	NULL,
    trail_estimated_time	Decimal(6,2)	NULL,
    CONSTRAINT            	TRAIL_PK 		PRIMARY KEY (trail_id)
    );
    
CREATE TABLE TRAILHEAD (
    trailhead_id            Integer         		NOT NULL,
    trailhead_longitude     Decimal(15, 12) 		NOT NULL,
    trailhead_latitude      Decimal(15, 12) 		NOT NULL,
    trailhead_directions    VarChar(500)      		NULL,
    CONSTRAINT            	TRAILHEAD_PK    		PRIMARY KEY (trailhead_id)
    );

CREATE TABLE TRAILHEAD_TRAILS (
	trailhead_id		Integer						NOT NULL, 
    trail_id			Integer						NOT NULL,
    CONSTRAINT          TT_PK    					PRIMARY KEY (trailhead_id, trail_id),
    CONSTRAINT			TRAILHEAD_Relationship		FOREIGN KEY (trailhead_id)
						REFERENCES					TRAILHEAD(trailhead_id),
    CONSTRAINT			TRAIL_TT_Relationship		FOREIGN KEY (trail_id)
						REFERENCES					TRAIL(trail_id)
    );
    
CREATE TABLE TRAIL_CONDITION (
    trail_id                Integer         	NOT NULL,
    condition_date          Date            	NOT NULL,
    condition_status        VarChar(50)        	NOT NULL,
    description             VarChar(500)      	NULL,
    CONSTRAINT              TRAIL_CONDITION_PK  PRIMARY KEY (trail_id, condition_date),
    CONSTRAINT            	Trail_Relationship 	FOREIGN KEY (trail_id)
							REFERENCES        	TRAIL (trail_id)
    );

CREATE TABLE PASS (
    pass_id				Integer  			NOT NULL,
    pass_type         	VarChar(50)        	NOT NULL,
    pass_price        	Decimal(8, 2)    	NULL,
    pass_duration     	Integer            	NULL,
    CONSTRAINT         	PASS_PK            	PRIMARY KEY (pass_id)
    );
    
CREATE TABLE TRAIL_PASS_OPTIONS (
    pass_id    		Integer         		NOT NULL,
    trail_id       	Integer            		NOT NULL,
    CONSTRAINT    	TRAIL_PASS_PK     		PRIMARY KEY (pass_id, trail_id),
    CONSTRAINT     	PASS_TP_RELATIONSHIP  	FOREIGN KEY (pass_id)
					REFERENCES     			PASS (pass_id),
    CONSTRAINT    	TRAIL_TP_RELATIONSHIP   FOREIGN KEY (trail_id)
					REFERENCES     			TRAIL (trail_id)
    );

CREATE TABLE TRAIL_POINT (
    point_id			Integer      				NOT NULL,
    trail_id           	Integer       				NOT NULL,
    point_longitude    	Decimal(15, 12) 			NOT NULL,
    point_latitude    	Decimal(15, 12) 			NOT NULL,
    point_elevation		Integer						NULL,
    CONSTRAINT        	TRAIL_POINT_PK    			PRIMARY KEY (point_id, trail_id),
    CONSTRAINT         	TRAIL_POINT_RELATIONSHIP 	FOREIGN KEY (trail_id)
						REFERENCES     				TRAIL (trail_id)
    );
    
CREATE TABLE FEATURE_TYPE (
    feature_name  		VarChar(20)         NOT NULL,
    description       	VarChar(500)  	 	NULL,
    CONSTRAINT       	FEATURE_PK 			PRIMARY KEY (feature_name)
    );

CREATE TABLE TRAIL_FEATURE (
    point_id               	Integer         				NOT NULL,
    trail_id              	Integer            				NOT NULL,
    feature_type          	VarChar(20)        				NOT NULL,
    point_feature_image 	VarChar(500) 					NULL,
    CONSTRAINT              TRAIL_PASS_PK        			PRIMARY KEY (point_id, trail_id, feature_type),
    CONSTRAINT            	TRAIL_TF_RELATIONSHIP    		FOREIGN KEY (trail_id)
							REFERENCES     					TRAIL (trail_id),
    CONSTRAINT            	POINT_TF_RELATIONSHIP    		FOREIGN KEY (point_id, trail_id)
							REFERENCES     					TRAIL_POINT (point_id, trail_id),
    CONSTRAINT            	FEATURE_TF_RELATIONSHIP    		FOREIGN KEY (feature_type)
							REFERENCES     					FEATURE_TYPE (feature_name)
    );
    
CREATE TABLE USER (
    user_id			Integer         NOT NULL	AUTO_INCREMENT,
    name        	VarChar(20)   	NULL,
    phone         	VarChar(15)    	NULL,
    email         	VarChar(30)    	NULL,
    CONSTRAINT    	USER_PK        	PRIMARY KEY (user_id)
    );
    
CREATE TABLE PREFERRED_FEATURE (
    user_id 			Integer         			NOT NULL,
    feature_name		VarChar(20)         		NOT NULL,
    CONSTRAINT			PREFERRED_FEATURE_PK 		PRIMARY KEY (user_id, feature_name),
    CONSTRAINT			FEATURE_PF_RELATIONSHIP 	FOREIGN KEY (feature_name)
						REFERENCES     				FEATURE_TYPE (feature_name),
    CONSTRAINT     		USER_PF_RELATIONSHIP    	FOREIGN KEY (user_id)
						REFERENCES     				USER (user_id)
    );
    
CREATE TABLE SAVED_TRAILS (
    user_id			Integer         		NOT NULL,
    trail_id   		Integer            		NOT NULL,
    CONSTRAINT  	SAVED_TRAILS_PK    		PRIMARY KEY (user_id, trail_id),
    CONSTRAINT    	TRAIL_ST_RELATIONSHIP 	FOREIGN KEY (trail_id)
					REFERENCES     			TRAIL (trail_id),
    CONSTRAINT   	USER_ST_RELATIONSHIP  	FOREIGN KEY (user_id)
					REFERENCES     			USER (user_id)
    );
    
CREATE TABLE USER_REVIEW (
    user_id           		Integer         		NOT NULL,
    trail_id              	Integer            		NOT NULL,
    date_reviewed          	Date            		NOT NULL,
    trail_rating        	Decimal(3,2)    		NOT NULL,
    review_text          	VarChar(500)      		NULL,
    CONSTRAINT            	REVIEW_PK        		PRIMARY KEY (user_id, trail_id, date_reviewed),
    CONSTRAINT            	TRAIL_UR_RELATIONSHIP    FOREIGN KEY (trail_id)
							REFERENCES     			TRAIL (trail_id),
    CONSTRAINT            	USER_UR_RELATIONSHIP    FOREIGN KEY (user_id)
							REFERENCES     			USER (user_id)
    );

CREATE TABLE COUNTY   (
    state				Char(2)				NOT NULL,
    county_id         	Integer        		NOT NULL,
    county_name       	VarChar(50)    		NULL,
    CONSTRAINT        	COUNTY_PK  			PRIMARY KEY (state, county_id)
    ); 
    
CREATE TABLE TRAIL_SEGMENT (
	trail_id			Integer  				NOT NULL, 
    state          		Char(2)					NOT NULL,
    county_id      		Integer    				NOT NULL,
    segment_length		Decimal(5,1)			NOT NULL,
    CONSTRAINT			SEGMENT_PK				PRIMARY KEY (trail_id, county_id, state),
    CONSTRAINT			TRAIL_TS_Relationship	FOREIGN KEY (trail_id)
						REFERENCES						TRAIL (trail_id),
	CONSTRAINT			COUNTY_STATE_TS_Relationship	FOREIGN KEY (state, county_id)
						REFERENCES						COUNTY (state, county_id)
);

CREATE TABLE JUNCTION (
    junction_id			Integer					NOT NULL,
    direction			Char(2)					NOT NULL,
    trail_id			Integer					NOT NULL,
    CONSTRAINT			JUNCTION_PK				PRIMARY KEY (junction_id, direction),
    CONSTRAINT			TRAIL_J_RELATIONSHIP	FOREIGN KEY (trail_id)
						REFERENCES				TRAIL (trail_id)
    );
    

INSERT INTO TRAIL VALUES (
	1, 'Cole Creek Falls', 2.2, 3, 2.5, 1.20 );
INSERT INTO TRAIL VALUES (
    2, 'Pacific Crest Trail', 2650, 10, 4.5, 1800.0 );
    
INSERT INTO TRAILHEAD VALUES (
    1, -122.130645, 47.536038, 'Take the 14th exit on 405, follow directions on the map, be careful when parking');
INSERT INTO TRAILHEAD VALUES (
    2, -115.7247223, 32.7998997, 'Take a right on 157th NW st');
INSERT INTO TRAILHEAD VALUES (
    3, -122.2265988, 48.9853907, 'Follow the signs for the Pacific Resort');
    
INSERT INTO TRAILHEAD_TRAILS VALUES (
    1, 1);
INSERT INTO TRAILHEAD_TRAILS VALUES (
    2, 2);
INSERT INTO TRAILHEAD_TRAILS VALUES (
    3, 2);

INSERT INTO TRAIL_CONDITION VALUES (
    1, '2023-12-31', 'closed', 'Closed due to overwhelming snow');
INSERT INTO TRAIL_CONDITION VALUES (
    1, '2024-03-15', 'active', NULL);
INSERT INTO TRAIL_CONDITION VALUES (
    2, '2024-07-12', 'closed', 'Trail Maintenence');
INSERT INTO TRAIL_CONDITION VALUES (
    2, '2024-07-25', 'active', 'Trail Maintenence complete');
    
INSERT INTO PASS VALUES (
	0, 'None', 0.00, null);
INSERT INTO PASS VALUES (
	1, 'Federal', 55.00, 365);
INSERT INTO PASS VALUES (
	2, 'State', 35.00, 365);
INSERT INTO PASS VALUES (
	3, 'State', 10.00, 1);

INSERT INTO TRAIL_PASS_OPTIONS VALUES (
    0, 1);
INSERT INTO TRAIL_PASS_OPTIONS VALUES (
    1, 2);
INSERT INTO TRAIL_PASS_OPTIONS VALUES (
    2, 2);
INSERT INTO TRAIL_PASS_OPTIONS VALUES (
    3, 2);

INSERT INTO TRAIL_POINT VALUES (
	1, 1, -122.1154276, 47.5254699, 1080 );
INSERT INTO TRAIL_POINT VALUES (
	1, 2, -121.5556868, 46.2276677, 5621);
INSERT INTO TRAIL_POINT VALUES (
	2, 1, -120.2368023, 39.1030507, 7775);
    
INSERT INTO FEATURE_TYPE VALUES (
    'Waterfall', 'Waterfall, Filter recommended when using as drinking water');
INSERT INTO FEATURE_TYPE VALUES (
   'View', 'Scenic View Platform, Binoculars Recommended');
INSERT INTO FEATURE_TYPE VALUES (
    'Wildlife', 'High chance of viewing local wildlife, please be considerate');
    
INSERT INTO TRAIL_FEATURE VALUES (
    2, 1, 'Wildlife', 'https://www.google.com/imgres?q=deer&imgurl=https%3A%2F%2F36x969.jpg&imgrefurl=https%3A%2F%2Fgeorgiarecorder.com%2F2023%2F07%2F11%2Fstate-wildlife-offoDCQQM3oECBsQAA');
INSERT INTO TRAIL_FEATURE VALUES (
    1, 2, 'View', 'https://www.google.com/imgres?q=scenic+view=aksopdjaskoe-askeSMEIQa1214.jpg');
INSERT INTO TRAIL_FEATURE VALUES (
    2, 1, 'Waterfall', 'https://www.google.com/imgres?q=swaterfall');
INSERT INTO TRAIL_FEATURE VALUES (
    1, 1, 'View', 'https://www.google.com/imgres?q=scenic');

INSERT INTO USER (name, phone, email) VALUES (
	'Kasey Kahne', '253-555-5555', 'kaseyraces@hotmail.com' );
INSERT INTO USER (name, phone, email) VALUES (
    'Sara Brooks', '503-555-5555', 'sara01@google.com' );
INSERT INTO USER (name, phone, email) VALUES (
    'Reese Witherspoon', '425-555-5555', 'hiker4life@yahoo.com');
    
INSERT INTO PREFERRED_FEATURE VALUES (
    1, 'View'); 
INSERT INTO PREFERRED_FEATURE VALUES (
    1, 'Wildlife');
INSERT INTO PREFERRED_FEATURE VALUES (
    1, 'Waterfall'); 
INSERT INTO PREFERRED_FEATURE VALUES (
    2, 'Waterfall');
INSERT INTO PREFERRED_FEATURE VALUES (
    2, 'Wildlife'); 
    
INSERT INTO SAVED_TRAILS VALUES (
    1, 1); 
INSERT INTO SAVED_TRAILS VALUES (
    1, 2);
INSERT INTO SAVED_TRAILS VALUES (
    3, 2);

INSERT INTO USER_REVIEW VALUES (
    1, 1, '2022-10-31', 5, 'Short but sweet! Loved it'); 
INSERT INTO USER_REVIEW VALUES (
    1, 2, '2022-11-15', 5, 'I saw a deer there, so cute!!1! '); 
INSERT INTO USER_REVIEW VALUES (
    2, 1, '2023-05-05', 1, 'Ruined my marriage'); 
INSERT INTO USER_REVIEW VALUES (
    3, 2, '2024-08-01', 4, 'Good trail, bad weather');

INSERT INTO COUNTY VALUES (
	'WA', 4, 'King');
INSERT INTO COUNTY VALUES (
	'CA', 23, 'El Dorado');
    
INSERT INTO TRAIL_SEGMENT VALUES (
	1, 'WA', 4, 2.2 );
INSERT INTO TRAIL_SEGMENT VALUES (
	2, 'WA', 4, 70.3 );
INSERT INTO TRAIL_SEGMENT VALUES (
	2, 'CA', 23, 20.4 );

INSERT INTO JUNCTION VALUES (
    1, 'NW', 1); 
INSERT INTO JUNCTION VALUES (
    1,'NE' , 2); 
INSERT INTO JUNCTION VALUES (
    1,'SE' , 1); 
INSERT INTO JUNCTION VALUES (
    1,' E' , 2);


# BASIC SCENARIOS
# Find a trail with 'fall' in the name
SELECT *
FROM TRAIL
WHERE trail_name LIKE '%fall%';

# Find a pass that is cheaper than fourty dollars and display in descending order
SELECT *
FROM PASS
WHERE pass_price < 40.0
ORDER BY pass_price DESC;

# View user reviews that have less than a 3 rating
SELECT *
FROM USER_REVIEW
WHERE trail_rating < 3.0;

# Find a trail with at least a rating of 4
SELECT *
FROM TRAIL
WHERE trail_rating >= 4.0;

# Display the trail details for user 1's saved trails
SELECT trail_name, trail_length, trail_difficulty, trail_rating, trail_estimated_time
FROM SAVED_TRAILS NATURAL JOIN TRAIL
WHERE user_id = 1;

# Have user 2 save a trail
INSERT INTO SAVED_TRAILS VALUES (
    2, 1); 

# Add a new user
INSERT INTO USER VALUES (
    4, 'Charles', '757-555-5555', 'cjoutside84@google.com' );
    
# Set user 4's preferred feature
INSERT INTO PREFERRED_FEATURE VALUES (
    4, 'View'); 

# Have user 4 create a trail review
INSERT INTO USER_REVIEW VALUES (
    4, 1, '2025-01-05', 3, 'The waterfall was beautiful, but no mountain views.');



# ANALYTICAL QUEREIS
# Get the latest trail condition for the Pacific Crest Trail
SELECT *
FROM TRAIL NATURAL JOIN TRAIL_CONDITION
WHERE trail_name = 'Pacific Crest Trail'
ORDER BY condition_date DESC
LIMIT 1;

# Find the cheapest pass for each trail. Display information for both.
SELECT T1.trail_id, trail_name, trail_rating, pass_id, min_price, pass_type, pass_duration
FROM 
    (SELECT T.trail_id, pass_id, min_price, pass_type, pass_duration
    FROM   (SELECT trail_id, MIN(pass_price) as min_price
        FROM TRAIL_PASS_OPTIONS NATURAL JOIN PASS
        GROUP BY trail_id) AS T JOIN TRAIL_PASS_OPTIONS ON TRAIL_PASS_OPTIONS.trail_id = T.trail_id
        NATURAL JOIN PASS WHERE pass_price = min_price) AS T1
        JOIN TRAIL ON TRAIL.trail_id = T1.trail_id;

# List the trails with user 2's preferred trail feature(s) and a difficulty no more than 3.
# Order the result by trail ID, then feature type.
SELECT *
FROM TRAIL NATURAL JOIN (
	SELECT trail_id, feature_type
	FROM TRAIL_FEATURE JOIN PREFERRED_FEATURE ON TRAIL_FEATURE.feature_type=PREFERRED_FEATURE.feature_name
	WHERE user_id = 2 ) AS user2_features
ORDER BY trail_id, feature_type;

# Display the average trail_rating for each user and number of reviews they've created, from most to least reviews per user
# as well as the user information for each user
SELECT *
FROM (SELECT user_id, AVG(trail_rating) as average_rating, COUNT(*) as review_count
FROM USER_REVIEW 
GROUP BY user_id) AS T NATURAL JOIN USER
ORDER BY review_count DESC;

-- Display all trails that are connected to the Pacific Crest Trail 
-- as well as the junction it is connected at, and what direction it goes to.
SELECT trail_name, junction_id, direction
FROM (
    SELECT *
    FROM TRAIL 
    WHERE trail_id in 
        (SELECT distinct junction_id
        FROM JUNCTION 
        WHERE trail_id = 
            (SELECT trail_id 
            FROM TRAIL 
            WHERE trail_name = 'Pacific Crest Trail') 
        ) 
    ) as T NATURAL JOIN JUNCTION ;

    


    

    

