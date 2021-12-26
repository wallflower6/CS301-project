4 KEY SERVICES
1. Experience Booking, See Venues
!!!DONE!!! - 1.1 GET /venues/{venue_id} 
    -> Fetch a venue by ID.
    -> venue_id String 
!!!DONE!!! - 1.2 Select Listing to View 

2. Experience Booking, Make Booking 
- 2.1 PUT /venues/{venue_id}/book
    -> Book a reservation at a Venue by ID.
    -> venue_id String     
- 2.2 Forward Booking Requests - return if can make booking or invalid 
!!!DONE!!! - 2.3 Forward Booking details - return confirmation 

3. Item Redemption, Redeem Item
- 3.1 Item redemption - forward redemption return redemption details
!!!DONE!!! - 3.2 Confirm item redemption details 

4. Registration, Create new user
!!!DONE!!! - 4.1 create membership account with password 

COMMANDS
- to run
mvnw spring-boot:run
- to test
curl http://localhost:8081/<what u need>
- to load webpage
http://localhost:8081/
- to run both instances at the same time to check



##THINGS TO CHECK
- EXPERIENCE BOOKING WANT TO ADD AVAIL TIMINGS??
- user can update the points