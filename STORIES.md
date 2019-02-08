# Briefing
## You're in the army now

At ease, private!

So you're the soldier who's programming the Java application that will help us manage the composition of our armies? 
I hope you know what you're doing, because this piece of software will contain a lot of sensitive information once it launches.
Our enemies know this too, so they'll be trying their damnedest to gain access to it. 
It's your job to prevent that from happening at all cost. The lives of a lot of good men depend on it!

Sarge McKnowitall told me the UI is already as secure as – and I quote – "a six foot foxhole". 
The Sarge isn't generous with his compliments, so you should feel honored, son!
However, your good work on the UI only upped our expectations of your work on the even more crucial backend code.

So get back to work and make your country proud, private! Oorah!

-- General Bradbury Cumberton III

## Some tactical notes

Next up are the stories you will be developing that will help you learn about securing modern Java applications.

- Start by forking the [switchfully-security repository](https://github.com/nielsjani/switchfully-security). 
- Then pull the code from your branched repository to your local machine.
- Finally, to build the code for the first time, call `mvnw.cmd clean install` from your command line

Each story has a 'starting point', a Git branch that has the minimum code setup for you to start the story without too much hassle.
This can come in handy because some stories build on top of each other. 
You don't have to switch to these branches every time you start a new story, but if you don't feel certain about your own code it might be safer to start from a branch.
 
Each story will have some tests already implemented (found under src/test/java/@name of the story@/@name of the story@Test.java).
However, we do expect you to write some additional (Unit) tests where you see fit.

Also, a REST-client such as [Postman](https://www.getpostman.com/) or [ARC](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo) might come in handy for some manual testing.

# (War) Stories
## Operation Avocado (starting point: [master](https://github.com/nielsjani/switchfully-security))

You're given a very basic Spring Boot application with a couple of REST-calls (see ArmyResource).
This app has an imaginary UI, but it is not relevant for these exercises. 
What you will be doing is securing the REST-calls in a number of ways using Spring's security features. 

Your first task is to make sure not just anyone can call the REST-API. 
We only want people using a Basic authentication header AND whose username and password combination are known to the system to be able to do REST-calls.

Getting started:

- Read [this](http://www.devglan.com/spring-security/spring-boot-security-rest-basic-authentication) article
- You can use an inMemoryAuthentication implementation for now to store your users and their passwords.
 This is not an advisable solution for real life applications, because there is no way for you to easily change/add/remove your users when the app is running.
 We will be switching to an LDAP in later exercises.
- The inMemoryAuthentication will have to hold a user called 'JMILLER' with password 'THANKS' in order for the AvocadoTest to pass.
- Since Spring Security 5, you need to have a passwordencoder present when creating users. 
This is to prevent you from storing passwords as plaintext. 
The inMemoryAuthentication will need this too, otherwise you'll get an error when running your tests.
  - For more info, read the following articles: 
    - [stackoverflow question about the error](https://stackoverflow.com/questions/46999940/spring-boot-passwordencoder-error),
    - [baeldung: spring seucrity 5 password storage](http://www.baeldung.com/spring-security-5-password-storage) and
    - [baeldung: registration - password encoding](http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt).
  - You can use SHA256 as an encoding algorithm. It's not the most secure (bcrypt is), but the encoding can easily be done online using
 [this](https://www.md5online.org/md5-encrypt.html) or [this](http://online-code-generator.com/md5-hash-with-optional-salt.php) site. 
  - !!!If you want to use a salt, make sure you put it AFTER the password!!!
 

Extra info:
- [What is a realm?](https://stackoverflow.com/questions/16186834/whats-the-meaning-of-realm-in-spring-security)

Extra assignment:
- AvocadoTest currently only tests the 'getDeployedArmyInfo' REST-call. Write similar tests for the other 3 REST-calls
- Try starting the application and call the rest endpoint using Advanced Rest Client or Postman. 
Experiment with correct and incorrect authorisation data and check if the app returns what you'd expect.

## Operation Baobab (starting point: [BAOBAB-START branch](https://github.com/nielsjani/switchfully-security/tree/BAOBAB-START))

Now that we've got authentication covered, let's move on the authorisation. 

Our app has four roles:

- CIVILIAN, who can join the army
- PRIVATE, who can deployed army info
- HUMAN_RELATIONSHIPS. He can promote privates and discharge soldiers
- GENERAL, who can do everything the PRIVATE can. He can also launch the nukes (but he'd rather not. Because he's not [MAD](https://en.wikipedia.org/wiki/Mutual_assured_destruction)).

On top of the existing authentication code, implement security that blocks authenticated users from accessing certain endpoints if they don't have the necessary roles.

Extra info:

- http://www.baeldung.com/spring-security-expressions-basic

Extra assignment:

- The article offers multiple ways to fix this problem. Try implementing both and see if they both do the job.
- Users can have multiple roles. Create a user who is both a PRIVATE and a HUMAN_RELATIONSHIPS and check if he can do everything both roles can.
- When using the 'antMatchers' method, create a couple of new rest endpoints:

    - GET /armies/tanks (getTanksInfo)
    - POST /armies/tanks (addTanks)
    - DELETE /armies/tanks (blowUpTanksAndEnjoyTheFireworks)
    - PUT /armies/tanks (addNewTanks)

you don't have to implement these methods. All of them can be void.

- The GET call should only be available to PRIVATE and GENERAL and CIVILIAN
- The POST call should only be available to PRIVATE 
- The DELETE call should only be available to GENERAL
- The PUT call should only be available to PRIVATE and GENERAL

Write some E2E tests up front to check your assumptions.

## Operation Cedar (starting point: [CEDAR-START branch](https://github.com/nielsjani/switchfully-security/tree/CEDAR-START))
In the previous story we implemented 'Role based'-security. There is another way to approach this, however: feature based security.

But first, let's switch to a userstore that's a bit closer to reality. Out goes the inMemoryAuthentication and in comes your very own authenticationProvider!

You will have to get rid of the inMemoryAuthentication in your Spring SecurityConfig class and replace it by a custom AuthenticationProvider.
This is a class that extends AuthenticationProvider. You will have to implement two methods: 'authenticate' and 'supports'.

### Implementing the authenticate() method
In the package 'external.authentication' you will find the class 'FakeAuthenticationService'. 
You will have to provide this class with a username and a password. 
If the combination is correct, this service will return an Object that contains the username, password and roles of the user.
If the combination is incorrect, it will return null.

The authenticate should return an 'Authentication' object if the username/password combo exists. 
Otherwise it should throw an exception that extends the AuthenticationException class.
Pick one you think fits the bill.
**HINT**: This method is a great candidate for some unit-tests.

### Implementing the supports() method
This method determines if the custom AuthenticationProvider will handle the Authentication request. In our case, the provider handles Username/Password Authenticationtokens.
The standard Java method 'isAssignableFrom()' might help you.

Note that no extra end-to-end tests have been provided for this story. 
That's because the existing behaviours aren't altered in any way. So after you're done, all the other tests should still run.

### Oh right, one more thing:

When using a custom AuthenticationProvider, Spring pretty much requires you to be using feature based security. 
To not make the story even bigger than it already is, we're gonna cheat a bit on that part (don't worry, we'll implement it properly in the next story).
To fix everything up, change your 'hasRole'- and 'hasAnyRole'-code in the ArmyResource and/or SecurityConfig to 'hasAuthority' and 'hasAnyAuthority'.
If everything is implemented correctly, both the baobabTest and the AvocadoTest should run.

Extra assignment:
Replace the passwords in the fakeAuthenticationService with hashed versions
When checking if a user's username/password combo is correct, you'll have to hash the password too 
http://www.baeldung.com/sha-256-hashing-java

## Operation Dogwood (starting point: [DOGWOOD-START branch](https://github.com/nielsjani/switchfully-security/tree/DOGWOOD-START))

Back to feature-based security. To get up to speed about the benefits feature-based security has, read [the following post on stackexchange](https://softwareengineering.stackexchange.com/questions/299729/role-vs-permission-based-access-control).

You've already completed one step along the way: using the 'hasAuthority' method/annotation.
Next, we'll have to create the different features and couple them to the role(s) that are allowed to access them.

Once you've got that coupling, you'll have to fill up the  your custom Authentication class's roles (GrandedAuthority's) correctly.
The FakeAuthenticationService will provide you with the role(s) of the logged in users. You'll have to map those roles to the allowed features manually.

Once again, there are no new end to end tests. The old tests should keep working after you're done, ofcourse.

Extra info:
- [Another article on role based vs feature based (called 'activity based' here)](https://lostechies.com/derickbailey/2011/05/24/dont-do-role-based-authorization-checks-do-activity-based-checks/)

## Operation Elm (starting point: [ELM-START branch](https://github.com/nielsjani/switchfully-security/tree/ELM-START))
There is one more way we can perform an extra check before allowing or denying access: using the 'access' method in the 'configure' method in our Spring security config file.
You've already used the 'antMatchers' method in Operation Baobab. If you try chaining the 'antMatchers()' method, you'll see you can call a method called 'access()' on it.

This method takes a Spring expression String using the following structure:
.access("@SpringBeanClass.methodOnThatClass(#nameOfSomeRestMethodParameter)");

An example:

.antMatchers(GET, "/my/rest/api/{somePathParam}").access("@antiHackerService.stopHackers(#somePathParam)");

The method that is called should return a boolean: true if the call should be allowed, false if it should be denied.

Your task is to stop people from getting promoted if they've got a criminal record. 
To check if someone has a criminal record, you can use the CriminalRecordService in the external package. 
Its method 'hasCriminalRecord' takes a username (a.k.a. the path param from the rest call) and returns a 'CriminalRecord' object.
If the 'offenses' on this object are empty, the person is 'clean'.

A couple of new accounts have been added to the FakeAuthenticationService, some of them with criminal records, others without. 

Extra assignment:
Implement the promotePrivate method:

A soldier can only be promoted by his supervisor

Create an in-memory structure that holds the link between a private and his supervisor (the supervisor has to be someone with the role PRIVATE or GENERAL)

add/alter an access check to the promotePrivate method that checks if a certain promotion can take place.

Then do the same for dischargeSoldier

# Extraction Point (for a look at the final code: [EXTRACTION-POINT branch](https://github.com/nielsjani/switchfully-security/tree/EXTRACTION-POINT))

# Operation Fir (starting point: [FIR-START branch](https://github.com/nielsjani/switchfully-security/tree/FIR-START))

Let's connect to a real LDAP to fetch our users. We will be using a publicly available test LDAP server. 
Connection info can be found [here](http://www.forumsys.com/tutorials/integration-how-to/ldap/online-ldap-test-server/), 
but also in the properties file found in this branch. 
You can connect directly with the LDAP using tools like [Apache Directory](http://directory.apache.org/), 
or you could take a look at the altered tests to see the expected results.

There is an easy way to [connect an LDAP with Spring](https://spring.io/guides/gs/authenticating-ldap/), but this won't work in our 
feature-based security context because we won't have a hook to assign features to the returned users (also, it wouldn't make for an interesting exercise). 
So we will have to create our own SpringSecurityLdapTemplate and do the necessary calls and mappings ourselves. Programming! Yay!

Let's start off with some new maven dependencies. You'll need:
- "org.springframework.ldap:spring-ldap-core:(version provided by spring parent)"
- "org.springframework.security:spring-security-ldap:5.0.7.RELEASE" (version not provided at the time of writing)

Now let's dive in our AuthenticationProvider. We no longer need to call the FakeAuthenticationService. 
Instead we'll need to construct a SpringSecurityLdapTemplate. 
This object takes an LdapContextSource as a parameter in its constructor. 
[Here's an example](https://gist.github.com/mpilone/7582628) to set one of these up.

You'll have to use the LdapTemplate now. It has a myriad of methods. A combination that fits our needs is:
- authenticate, an method that takes a filter (you can filter on uid) and a password.
If the combination is correct, it will pass the corresponding user to the third parameter, a mapping function. 
Otherwise, it will throw an exception.
- searchForSingleAttributeValues. The problem with authenticate is that it does not return the group(s) the user is part of.
That's what this method is for. 
It takes a number of parameters: 
  - 'base': you can pass an empty string here
  - 'filter': should be (uniqueMember={0})
  - an array of params: should look something like this "uid=username,dc=example,dc=com", "username"
  - the attributename we would like to know: 'cn'
 
Having gathered all this data, we need to map it to an Authentication object, like we did with the FakeAuthenticationService. 
All users in the test LDAP are part of at least one and at most two groups (= roles). 
Those groups don't match the ones we've defined so far, so here's a mapping from the test LDAP's roles to ours:
- MATHEMATICIANS => GENERAL
- CHEMISTS => PRIVATE
- SCIENTISTS => CIVILIAN
- ITALIANS => HUMAN_RELATIONSHIPS

Alter the Feature class, so the test LDAP roles have correct access to each endpoint.

If you're stuck, debugging a test that should return a HTTP 200 might help you to see what's wrong. 
The hardest part of this exercise is getting the calls to the LDAP to work. 
Also, Google probably is not your friend on this one...

You can check [Operation Gorse](https://github.com/nielsjani/switchfully-security/tree/FIR-GORSE) for a solution to this story.

Congratulations, [a winner is you](http://i0.kym-cdn.com/photos/images/facebook/000/048/783/a_winner_is_you20110724-22047-1nd3wif.jpg)!
You've completed all stories and made the lives of your fellow countrymen a little safer. 
See any of your fellow students struggling? 
Try giving them a helping hand (don't just mail them your code, nobody learns anything from blindly copy-pasting text from one app to another).


