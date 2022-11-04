var express = require('express');
const mongoose = require("mongoose");
var router = express.Router();
const User = require("../models/User");

/* GET home page. */
router.get('/', async(req, res) => {
  res.send('hello world');


});

module.exports = router;
