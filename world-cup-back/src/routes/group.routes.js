const express = require('express');
const groupController = require('../controllers/group.controllers');

const router = express.Router();

router.get('/groups', groupController.findAllGroups);

module.exports = router;
