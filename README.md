# Free Now Taxi Test Application
A Kotlin android app with Clean Architecture and MVVM Architecture pattern.

## Libraries used
1. Dagger2 - For Dependency Injection
2. Android Architecture Components
3. RxJava2
4. Retrofit - For making API Request 
5. DroidNet - For Internet Checking
6. Google Maps
7. Nhaarman Mockito for Testing

# Use cases
After opening the Application, app will make a API call for fetching the PoiList
For Task 1 - 
    On Successful response showing all data in Taxi List.
For Task 2 - 
    Same response is used for showing taxi data on Google map.
    You may find Taxi are tilted in the map, it is because of the heading angle. 
    Marker added on two point as Point P1 and P2 whose latitude and longitude are provided in the problem statement.    
    Whenever we try to move map I calculate center of the map and that center point 
    will be next Point P2, the point P2 in last API request will be point P1, So we get two point 
    and make API request for fetching the data again to plot it on the map.
    Smaller the movement on map denser the markers on map, higher the movement on map less denser the markers on the map.   
    If user click on either of the marker or Taxi, that will come in center and set text will display.
    For convenience I also provide bottom sheet where you can filter which type of Taxi you want to 
    see on the map.  
    One Extra switch is also provided if we don't want to fetch taxi on map movement.
    Internet connectivity is handled with the Snackbar and Toast message in ongoing API request.  

## Screenshots
Application detailed screenshots has been added for understanding the complete App Use. 
