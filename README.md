# 🌍 Earthquake Assignment

A full-stack application that fetches real-time earthquake data from the **USGS GeoJSON API**, filters it, stores it in a PostgreSQL database, and displays it through a React frontend.

## Features

- Fetch the latest earthquake data from USGS
- Filter earthquakes by magnitude and time
- Store data in a PostgreSQL database
- Display data in an interactive frontend table
- Delete individual earthquake records
- Map visualization of earthquake locations

---

## Tech Stack

| Layer     | Technology         |
|-----------|--------------------|
| Backend   | Spring Boot (Java) |
| Database  | PostgreSQL         |
| Frontend  | React + Vite       |
| UI        | Bootstrap          |
| Data Source | USGS GeoJSON API |

---

## Getting Started

### Prerequisites

- Java (JDK 17+ recommended)
- Node.js & npm
- PostgreSQL
- IntelliJ IDEA (or any Java IDE)

---

### 1. Clone the Repository

```bash
git clone https://github.com/AngelRis/Earthquake_Assigment.git
cd Earthquake_Assigment
```

---

### 2. Backend Setup (Spring Boot)

1. Start your PostgreSQL server on port **5432**
2. Create a database named `earthquake_db`
3. Create a user with username `postgres` and set your password
4. Update `application.properties` if your database name or credentials differ
5. Open the project in **IntelliJ IDEA**
6. Run the application in **Run** or **Debug** mode

The backend will be available at: **http://localhost:8080**

Tests are located in the `test` directory, inside the `EarthquakeServiceTests` class.

---

### 3. Frontend Setup (React)

```bash
cd frontend
npm install
npm run dev
```

The frontend will be available at: **http://localhost:5173**

---

## Assumptions

- The USGS API always returns a valid GeoJSON format
- Only earthquakes with magnitude **greater than 2.0** are processed and stored
- Data is considered relevant only for the **last 1 hour**
- Existing database records are cleared before inserting new data to avoid duplicates
- The application runs in a **local development environment**

---

## Optional Improvements

- 🗺️ **Map Visualization** — earthquake locations displayed on an interactive map
- 🗑️ **Record Deletion** — ability to delete a specific earthquake record
- 🎨 **Improved UI** — responsive layout built with Bootstrap
