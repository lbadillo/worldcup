const groupService = require('../services/group.service');

const findAllGroups = async (req, res) => {
  try {
    const groups = await groupService.getAllGroups();
    res.status(200).json(groups);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

module.exports = {
  findAllGroups,
};
