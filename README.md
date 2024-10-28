# Fetch Android-App-data-retriever

This Android application displays data fetched from a JSON API, organizing items under expandable and collapsible sections based on their `listId` and whether they have valid or invalid `name` values.

---

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the App](#running-the-app)
- [Project Structure](#project-structure)
- [Usage Instructions](#usage-instructions)


---

## Introduction

This application demonstrates how to:

- Fetch JSON data from a user-provided URL.
- Parse and process the data.
- Organize items into sections based on `listId`.
- Separate items into valid and invalid sections.
- Implement expandable and collapsible sections in a `RecyclerView`.
- Display the data in a user-friendly manner.

---

## Features

- Dynamic Data Fetching: Users can input any valid JSON URL to fetch data.
- Data Parsing: Parses JSON data and separates items into valid and invalid groups.
- Expandable Sections: Users can expand or collapse sections for each `listId`.
- Invalid Items Display: Items with null or empty `name` values are displayed under "Invalid Items".
- Sorting: Items are sorted based on `listId` and numeric value extracted from `name`.
- Progress Indicator: A `ProgressBar` indicates data loading progress.

---

## Prerequisites

- Android Studio*: Version 4.0 or higher.
- Android SDK: API Level 21 (Android 5.0 Lollipop) or higher.
- Internet Connection: Required for fetching data from the URL.
- Emulator: Pixel 3a API 34 extension level 7 x86 64

---
## Installation

### 1. Clone the Repository
Clone or download the project to your local machine:
git clone https://github.com/JKKO0/Android-App-data-retriever.git


### 2. Open the Project in Android Studio
- Launch Android Studio.
- Click on **File** -> **Open**.
- Navigate to the cloned project directory and select it.

### 3. Sync Gradle Files
- Android Studio should automatically detect and sync Gradle files.
- If not, click on **File** -> **Sync Project with Gradle Files**.

### 4. Add Missing Libraries or Resources
Ensure that all required resources, such as drawable assets, are present. If you encounter any missing resource errors, add them as described in the project files.

## Running the App

### 1. Set Up an Emulator or Device
- **Emulator**: Use the Android Virtual Device (AVD) Manager to create an emulator.
- **Physical Device**: Enable USB debugging on your Android device and connect it to your computer.

### 2. Run the App
- Click on the green "Run" button in Android Studio or press **Shift + F10**.
- Select your emulator or connected device.
- The app will build and install on the selected device.

## Project Structure

- **MainActivity.java**: The main activity that handles UI interactions and data fetching.
- **ItemAdapter.java**: Adapter for the RecyclerView, handling item views and header views.
- **Item.java**: Model class representing an item from the JSON data.
- **HeaderItem.java**: Model class representing a header in the list.
- **ListItem.java**: Interface implemented by Item and HeaderItem for the adapter.

### Layouts
- **activity_main.xml**: Layout for the main activity.
- **header_layout.xml**: Layout for header items.
- **item_layout.xml**: Layout for regular items.

### Drawable Resources
- **ic_arrow_down.xml**: Vector asset for the expand/collapse arrow icon.

## Usage Instructions

### 1. Launch the App
The app will open with a default URL pre-filled in the text box.

### 2. Enter a URL (Optional)
You can enter any valid JSON URL that returns data in the expected format.  
**Example URL**: `https://fetch-hiring.s3.amazonaws.com/hiring.json`

### 3. Fetch Data
- Tap the "Display" button to fetch and display data from the URL.
- A ProgressBar will appear, indicating that data is being loaded.

### 4. View Data
The data is displayed in a RecyclerView, organized into sections. Each `listId` has two sections:
- **Valid Items**: Items with non-null, non-empty name values.
- **Invalid Items**: Items with null or empty name values.

### 5. Expand/Collapse Sections
- Tap on a header (e.g., "List ID: 1 - Valid Items") to expand or collapse that section.
- The arrow icon next to the header indicates the current state:
  - **Downward arrow**: Section is expanded.
  - **Rightward arrow**: Section is collapsed.

### 6. Interact with Items
- Scroll through the list to view all items.
- Items display their ID and Name values.

---
