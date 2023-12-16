# Smart Parking Finder Mobile Application

## Introduction

The Smart Parking Finder Application is designed to help students and staff at Worcester Polytechnic Institute locate available parking spaces efficiently. Utilizing real-time data and image processing, it provides a user-friendly interface to navigate the often challenging task of parking on campus.


## Features

- Real-time parking space availability
- User-friendly navigation interface
- Database management for user data
- Analytical reports on parking usage and trends

## Prerequisites

Before running the application, ensure you have the following installed:
- Python 3.8 or higher
- For proper access to Firebase a working internet connection is needed
- Camera should be set up ... 


## Installation

To set up the application on your local system, follow these steps:
### Android (User-side)
1. Clone the repository to your local machine:
   ```sh
   git clone https://github.com/rumu10/CS528FinalProject-ParkingFinder

2.

## File Summaries

### Geofencing Files Detail
- `GeofenceBroadcastReceiver` manages geofence triggers, fetches parking data from Firebase upon entry to a specific area, and notifies users about available parking spaces nearby through notifications.
- `GeofenceHelper` class aids in setting up geofencing functionalities. It constructs geofencing requests, defines geofences based on location and parameters, creates a pending intent for geofence events.
- `GeofenceService ` is a background Service managing geofencing operations. It sets up geofences based on specified parameters, requests location permissions if needed, and adds geofences using the GeofencingClient.
- `NavigationView` class handles map-related features:
  + Initializes Places API and sets up the map.
  + Marks locations, retrieves user location, and draws route polylines.
  + Manages permissions for location services and geofence additions based on user interactions.
