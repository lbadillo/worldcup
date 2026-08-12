const Group = require('../models/group.model');

const createGroup = async (groupData) => {
  const group = new Group(groupData);
  return await group.save();
};

const getAllGroups = async () => {
  return await Group.find().lean();
};

const getGroupById = async (groupId) => {
  return await Group.findById(groupId);
};

const updateGroup = async (groupId, groupData) => {
  return await Group.findByIdAndUpdate(groupId, groupData, { new: true });
};

const deleteGroup = async (groupId) => {
  return await Group.findByIdAndDelete(groupId);
};

module.exports = {
  createGroup,
  getAllGroups,
  getGroupById,
  updateGroup,
  deleteGroup,
};
