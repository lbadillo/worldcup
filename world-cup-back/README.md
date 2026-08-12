# World Cup Backend

This is the backend API for the World Cup project. It is built with Express.js and MongoDB using Mongoose.

## Tech Stack

- Node.js
- Express
- MongoDB + Mongoose
- dotenv
- Nodemon

## Project Structure

```bash
src/
├── app.js
├── server.js
├── config/
│   ├── db.js
│   └── env.js
├── controllers/
│   └── group.controllers.js
├── models/
│   └── group.model.js
├── routes/
│   └── group.routes.js
├── services/
│   └── group.service.js
```

## Prerequisites

- Node.js 18+
- npm
- MongoDB instance or MongoDB Atlas connection string

## Installation

1. Clone the repository.
2. Navigate to the project folder.
3. Install dependencies:

```bash
npm install
```

## Environment Variables

Create a `.env.local` file in the project root with the following:

```bash
PORT=3000
MONGODB_URI=mongodb://localhost:27017/worldcup
```

The app loads the environment based on `NODE_ENV`, defaulting to `local`.

## Available Scripts

```bash
npm run dev
```

Starts the server with nodemon for local development.

```bash
npm run local
```

Starts the app with `NODE_ENV=local`.

```bash
npm start
```

Starts the application in production mode.

## API Endpoints

### Groups

- `GET /api/groups` - Get all groups

Example response:

```json
[
  {
    "_id": "64f1c2d3e4f5a6b7c8d9e0f1",
    "id": "group-a",
    "name": "Group A",
    "teams": [
      {
        "id": "arg",
        "name": "Argentina",
        "flag": "https://example.com/arg.png",
        "countryCode": "AR",
        "wins": 0,
        "looses": 0,
        "draws": 0
      }
    ]
  }
]
```

## Running the Server

```bash
npm run dev
```

The server will run on the configured port, usually:

```bash
http://localhost:3000
```

## Notes

- The application listens on `/api` and currently exposes the groups route.
- The MongoDB connection is established in `src/config/db.js`.
- The project is intentionally lightweight and follows a simple MVC-style structure.
