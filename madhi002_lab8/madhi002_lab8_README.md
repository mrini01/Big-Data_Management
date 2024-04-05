# Lab 8

## Student information

* Full name: Mrinisha Adhikari
* E-mail: madhi002
* UCR NetID: madhi002
* Student ID: 862309931

## Answers

* (Q1) What is the schema of the file? Copy it to the README file and keep it for your reference.
root                                                                            
 |-- hashtags: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- id: long (nullable = true)
 |-- lang: string (nullable = true)
 |-- place: struct (nullable = true)
 |    |-- country_code: string (nullable = true)
 |    |-- name: string (nullable = true)
 |    |-- place_type: string (nullable = true)
 |-- text: string (nullable = true)
 |-- time: string (nullable = true)
 |-- user: struct (nullable = true)
 |    |-- followers_count: long (nullable = true)
 |    |-- statuses_count: long (nullable = true)
 |    |-- user_id: long (nullable = true)
 |    |-- user_name: string (nullable = true)


* (Q2) What is your command to import the `tweets.json` file?

    ```shell
    # Replace here
    mongoimport --collection=tweets tweets.json
    ```

* (Q3) What is the output of the import command?

    ```text
    # Replace here
    2024-02-26T14:07:55.003-0800	connected to: mongodb://localhost/
    2024-02-26T14:07:55.047-0800	1000 document(s) imported successfully. 0 document(s) failed to import.
    ```

* (Q4) What is your command to count the total number of records in the `tweets` collection and what is the output of the command?

    ```javascript
    // Replace here
    db.tweets.find().count()
    ```

* (Q5) What is your command for this query?

    ```javascript
    // Replace here
    db.tweets.find({"place.country_code": "JP", "user.statuses_count": {$gt: 50000}},{"user.user_name": 1, "user.followers_count": 1, "user.statuses_count": 1 }).sort({"user.followers_count": 1})
    ```

* (Q6) How many records does your query return?
My query returned 16 records.

* (Q7) What is the command that retrieves the results without the _id field?

    ```javascript
    // Replace here
    db.tweets.find({"place.country_code": "JP", "user.statuses_count": {$gt: 50000}},{"_id": "0", "user.user_name": 1, "user.followers_count": 1, "user.statuses_count": 1 }).sort({"user.followers_count": 1})
    ```

* (Q8) What is the command to insert the sample document? What is the result of running the command?

    ```javascript
    // Replace here - command:
    db.tweets.insertOne({id: Long('921633456941125634'), place: { country_code: 'JP', name: 'Japan', place_type: 'city' }, user: {user_name: 'xyz2', followers_count: [2100, 5000], statuses_count: 55000}, hashtags: ['nature' ],lang: 'ja'})
    ```

    ```javascript
    // Replace here - results:
    {
	  acknowledged: true,
 	 insertedId: ObjectId('65dd1becd2ee295f2fbf10ef')
    }
    ```


* (Q9) Does MongoDB accept this document while the followers_count field has a different type than other records?
Yes, MongoDB does accept the document. 

* (Q10) What is your command to insert this record?

    ```javascript
    // Replace here
    db.tweets.insertOne({id: Long('921633456941121354'), place: { country_code: 'JP', name: 'Japan', place_type: 'city' }, user: {user_name: 'xyz3', followers_count: {last_month: 550, this_month: 2200}, statuses_count: 112000}, hashtags:     [ 'art', 'tour' ], lang: 'ja'})
    ```

* (Q11) Where did the two new records appear in the sort order?
There are now 18 records printed in descending sort order. The first user that was inserted, 'zyz2', appears as the 4th record (in between users ssasachan and mgn_sapporo). The second user that was inserted, 'zyz3', appears at
the very beginning (1st record).

Output:
[
  {
    _id: ObjectId('65dd1f22d2ee295f2fbf10f0'),
    user: {
      user_name: 'xyz3',
      followers_count: { last_month: 550, this_month: 2200 },
      statuses_count: 112000
    }
  },
.
.
.
 {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c9d'),
    user: {
      user_name: 'ssasachan',
      followers_count: 6745,
      statuses_count: 111304
    }
  },
  {
    _id: ObjectId('65dd1becd2ee295f2fbf10ef'),
    user: {
      user_name: 'xyz2',
      followers_count: [ 2100, 5000 ],
      statuses_count: 55000
    }
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00d80'),
    user: {
      user_name: 'mgn_sapporo',
      followers_count: 4578,
      statuses_count: 52056
    }
  },
 

* (Q12) Why did they appear at these specific locations?
User zyz2's followers_count is of type array, listed as: [2100, 5000]. While, zyz3's followers_count is an object, listed as: {last_month: 500, this_month: 2200}. Through the documnetation of MongoDB site, we find that when comparing 
values of different BSON types, objects rank higher than arrays do. So, zyz3 appears at the top, then zyz2's array value is just compared to the rest of the id values and sorted accordingly.  


* (Q13) Where did the two records appear in the ascending sort order? Explain your observation.
When sorted in an ascending order (smallest to highest), zyz2 appears as the 11th record (in between users mgm_aomori and mgm_asahikawa), and user zyz3 is appears at the very last (18th record). Once again, through the documnetation of 
MongoDB site, we find that when comparing values of different BSON types, objects rank higher than arrays do. So this checks out, as zyz3 has followers_count as object type which is ranked higher than array, its at the bottom (highest 
placement since its ascending), and zyz2's array value is just compared to the rest of the id values and sorted accordingly.  

Output:
.
.
.
 {
    user: {
      user_name: 'mgn_aomori',
      followers_count: 2049,
      statuses_count: 52624
    },
    _id: '0'
  },
  {
    user: {
      user_name: 'xyz2',
      followers_count: [ 2100, 5000 ],
      statuses_count: 55000
    },
    _id: '0'
  },
  {
    user: {
      user_name: 'mgn_asahikawa',
      followers_count: 2197,
      statuses_count: 51928
    },
    _id: '0'
  },
.
.
.
  {
    user: {
      user_name: 'xyz3',
      followers_count: { last_month: 550, this_month: 2200 },
      statuses_count: 112000
    },
    _id: '0'
  }
]
  

* (Q14) Is MongoDB able to build the index on that field with the different value types stored in the `user.followers_count` field?
Yes, MongoDB was able to build the index on the "user.followers_count" field with different value types stored in it. 

* (Q15) What is your command for building the index?

    ```javascript
    // Replace here
    db.tweets.createIndex({"user.followers_count": 1})
    ```

* (Q16) What is the output of the create index command?

    ```text
    user.followers_count_1
    ```

* (Q17) What is your command for this query?

    ```javascript
    // Replace here
    db.tweets.find({"hashtags": {$in: ['job', 'hiring', 'IT']}},{"text": 1, "hashtags": 1, "user.user_name": 1, "user.followers_count": 1}).sort({"user.followers_count": 1})
    ```

* (Q18) How many records are returned from this query?

    ```
    // Replace here
    I got 20 records at first. It also prompted me to "Type "it" for more", after doing so, i got 4 more. So a total of 24 records:

    [
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00b2d'),
    text: 'See our latest #Richmond, KY #job and click to apply: Pharmacy Technician - 2013 Lantern Ridge DRive... - https://t.co/Z24j8Ra0rX #Hiring',
    user: { user_name: 'MeijerJobs', followers_count: 116 },
    hashtags: [ 'Richmond', 'job', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00d10'),
    text: 'Can you recommend anyone for this #job? RN - Critical Care - https://t.co/9kTXnlPf9d #RN #NurseHero #Nursing #Kinston, NC #Hiring',
    user: { user_name: 'UNCLenoirJobs', followers_count: 124 },
    hashtags: [ 'job', 'RN', 'NurseHero', 'Nursing', 'Kinston', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00a35'),
    text: 'Interested in a #job in #LittleRock, AR? This could be a great fit: https://t.co/XyK7sPml0m #Nursing #Hiring',
    user: { user_name: 'uamsnurses', followers_count: 139 },
    hashtags: [ 'job', 'LittleRock', 'Nursing', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00cde'),
    text: "We're #hiring! Read about our latest #job opening here: QC Virology Technician (Fixed-Term) - https://t.co/4LUwyXfFf0 #Engineering",
    user: { user_name: 'tmj_ukg_eng', followers_count: 266 },
    hashtags: [ 'hiring', 'job', 'Engineering' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00d77'),
    text: "We're #hiring! Click to apply: Lab technician - https://t.co/rbiLZaVc2F #Engineering #Cambridge, England #Job #Jobs #CareerArc",
    user: { user_name: 'tmj_CAM_eng', followers_count: 293 },
    hashtags: [
      'hiring',
      'Engineering',
      'Cambridge',
      'Job',
      'Jobs',
      'CareerArc'
    ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c60'),
    text: 'Join the General Electric team! See our latest #job opening here: https://t.co/ClraXw9hPZ #Manufacturing #HambleleRice, England #Hiring',
    user: { user_name: 'tmj_GBR_manuf', followers_count: 294 },
    hashtags: [ 'job', 'Manufacturing', 'HambleleRice', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00a65'),
    text: "We're #hiring! Click to apply: Elektrotechniker (m/w) - https://t.co/NK7yVtKGEi #Manufacturing #Celle, NDS #Job #Jobs #CareerArc",
    user: { user_name: 'tmj_ger_manuf', followers_count: 298 },
    hashtags: [ 'hiring', 'Manufacturing', 'Celle', 'Job', 'Jobs', 'CareerArc' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00d7f'),
    text: "Want to work at QuintilesIMS? We're #hiring in #GreatBritain! Click for details: https://t.co/a5sVzotGdi #Pharmaceutical #Job #Jobs",
    user: { user_name: 'tmj_she_pharm', followers_count: 329 },
    hashtags: [ 'hiring', 'GreatBritain', 'Pharmaceutical', 'Job', 'Jobs' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c89'),
    text: 'Join the QuintilesIMS team! See our latest #job opening here: https://t.co/rT8i2sUyaS #HR #GreatBritain #Hiring #CareerArc',
    user: { user_name: 'tmj_she_hr', followers_count: 331 },
    hashtags: [ 'job', 'HR', 'GreatBritain', 'Hiring', 'CareerArc' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c8a'),
    text: "Want to work at Gap Inc.? We're #hiring in #Bath, England! Click for details: https://t.co/vVnfi2nZCn #Seasonal #Job #Jobs #CareerArc",
    user: { user_name: 'tmj_UKO_retail', followers_count: 378 },
    hashtags: [ 'hiring', 'Bath', 'Seasonal', 'Job', 'Jobs', 'CareerArc' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00cbb'),
    text: 'Join the Thermo Fisher Scientific team! See our latest #job opening here: https://t.co/flYMojBGI9 #SkilledTrade #Oxford, England #Hiring',
    user: { user_name: 'tmj_GBR_skltrd', followers_count: 392 },
    hashtags: [ 'job', 'SkilledTrade', 'Oxford', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00b2e'),
    text: 'See our latest #Antrim, Northern Ireland #job and click to apply: Seasonal Sales Associate - Antrim - https://t.co/0KfEaXgoSZ #Seasonal',
    user: { user_name: 'tmj_GBR_retail', followers_count: 462 },
    hashtags: [ 'Antrim', 'job', 'Seasonal' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c5f'),
    text: "We're #hiring! Read about our latest #job opening here: Rails Developer - https://t.co/jtQPOvgY7g #IT #Cambridge, England #CareerArc",
    user: { user_name: 'tmj_CAM_it', followers_count: 484 },
    hashtags: [ 'hiring', 'job', 'IT', 'Cambridge', 'CareerArc' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00dbc'),
    text: 'Interested in a #job in #Florence, Tuscany? This could be a great fit: https://t.co/iADBCQkFeH #internship #Hiring #CareerArc',
    user: { user_name: 'tmj_ita_jobs', followers_count: 671 },
    hashtags: [ 'job', 'Florence', 'internship', 'Hiring', 'CareerArc' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00bfa'),
    text: 'Interested in a #job in #HongKong? This could be a great fit: https://t.co/Ac99etPtgB #architecture #Hiring #CareerArc',
    user: { user_name: 'tmj_hkg_jobs', followers_count: 758 },
    hashtags: [ 'job', 'HongKong', 'architecture', 'Hiring', 'CareerArc' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00ab8'),
    text: 'Can you recommend anyone for this #job? Erfahrener Klinikreferent (m/w): Trier, Mainz,... - https://t.co/xScFgin33f #Pharmaceutical',
    user: { user_name: 'QuintilesIMSjob', followers_count: 774 },
    hashtags: [ 'job', 'Pharmaceutical' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c5c'),
    text: "Want to work at Federal-Mogul Motorparts? We're #hiring in #Shanghai! Click for details: https://t.co/mCO6JYqiM4 #Sales #Job #Jobs",
    user: { user_name: 'tmj_chs_jobs', followers_count: 814 },
    hashtags: [ 'hiring', 'Shanghai', 'Sales', 'Job', 'Jobs' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00ab9'),
    text: "If you're looking for work in #Houston, TX, check out this #job: https://t.co/kYaAz0j9JK #CustServ #CustomerCare… https://t.co/gzgA25XTe2",
    user: { user_name: 'GDKN', followers_count: 831 },
    hashtags: [ 'Houston', 'job', 'CustServ', 'CustomerCare' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c90'),
    text: 'Can you recommend anyone for this #job? Java Developer - https://t.co/St1wWRFEFz #Tech #IoT #digital #InfoTech… https://t.co/BT6nFvbfWF',
    user: { user_name: 'GDKN', followers_count: 831 },
    hashtags: [ 'job', 'Tech', 'IoT', 'digital', 'InfoTech' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c91'),
    text: 'See our latest #Twinsburg, OH #job and click to apply: Electronic Solder Assembler - https://t.co/sSjAmUdLFK… https://t.co/M83P3UVUND',
    user: { user_name: 'GDKN', followers_count: 831 },
    hashtags: [ 'Twinsburg', 'job' ]
  }
]
Type "it" for more
.
.
.
[
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00cc2'),
    text: 'Interested in a #job in #GardenGrove, CA? This could be a great fit: https://t.co/jezNLsDl1s #FinTech #FinServ… https://t.co/mcPlZJwwxR',
    user: { user_name: 'GDKN', followers_count: 831 },
    hashtags: [ 'job', 'GardenGrove', 'FinTech', 'FinServ' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00bb1'),
    text: 'Interested in a #job in #Hillsboro, OH? This could be a great fit: https://t.co/pnsZAPXZzd #HomeHealth #PatientCare #RN #Hiring',
    user: { user_name: 'nurserehabjobs', followers_count: 1019 },
    hashtags: [ 'job', 'Hillsboro', 'HomeHealth', 'PatientCare', 'RN', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00b2c'),
    text: 'Can you recommend anyone for this #job in #GardenCity, NY? https://t.co/6tURhfAbTv #Simon #CustomerService #Hiring',
    user: { user_name: 'SimonCareers', followers_count: 1500 },
    hashtags: [ 'job', 'GardenCity', 'Simon', 'CustomerService', 'Hiring' ]
  },
  {
    _id: ObjectId('65dd0bbb3cb8dc3320f00c8e'),
    text: 'Can you recommend anyone for this #job in Đà Nẵng? https://t.co/EgvD7Tz6ry #manager #Hiring',
    user: { user_name: 'ManulifeJobs', followers_count: 3600 },
    hashtags: [ 'job', 'manager', 'Hiring' ]
  }
]
    ```

* (Q19) What is your command for this query?

    ```javascript
    // Replace here
    db.tweets.aggregate([{ $group: {_id: "$place.country_code", totalTweets: { $sum: 1}}}, { $sort: {totalTweets: -1}}, { $limit: 5}])
    ```

* (Q20) What is the output of the command?
Output:
[
  { _id: 'US', totalTweets: 153 },
  { _id: 'JP', totalTweets: 107 },
  { _id: 'GB', totalTweets: 89 },
  { _id: 'TR', totalTweets: 65 },
  { _id: 'IN', totalTweets: 56 }
]


* (Q21) What is your command for this query?

    ```javascript
    // Replace here
    db.tweets.aggregate([{ $unwind: "$hashtags"}, { $group: {_id: "$hashtags", count: { $sum: 1}}}, { $sort: { count: -1}}, { $limit: 5}])
    ```

* (Q22) What is the output of the command?
Output:
[
  { _id: 'ALDUBxEBLoveis', count: 56 },
  { _id: 'no309', count: 31 },
  { _id: 'LalOn', count: 31 },
  { _id: 'FurkanPalalı', count: 31 },
  { _id: 'job', count: 19 }
]

