const mongoose = require('mongoose');

const TeamSchema = new mongoose.Schema({
  id: {
    type: String,
  },
  name: {
    type: String,
  },
  flag: {
    type: String,
  },
  countryCode: {
    type: String,
  },
  wins: {
    type: Number,
    default: 0,
  },
  looses: {
    type: Number,
    default: 0,
  },
  draws: {
    type: Number,
    default: 0,
  },
});

const GroupSchema = new mongoose.Schema({
  id: {
    type: String,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  teams: [TeamSchema],
});

module.exports = mongoose.model('Group', GroupSchema);
