const express = require('express');
const groupRoutes = require('./routes/group.routes');

const app = express();

app.use(express.json());
app.use('/api', groupRoutes);

module.exports = app;
