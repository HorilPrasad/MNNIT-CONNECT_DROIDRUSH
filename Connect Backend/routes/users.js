var express = require('express');
var router = express.Router();


//const { app } = require('firebase-admin');
const UserController = require('../controllers/users');
const verify = require('../middleware/verify-token')

/* GET users listing. */
router.get('/', (req, res, next) => {
  res.send('respond with a resource');
});

//register new user
router.post('/register', UserController.user_register);

//login user
router.post('/login', UserController.user_login);

//verify user email
router.get('/verify/:id', UserController.user_verify);

// //finding one user
router.get('/users/:id', UserController.user_find_one)

// //getting all user
// router.get('/users/', verify, UserController.user_find_all)

//create profile
router.post('/create/:id', UserController.user_create_profile)

// //delete profile
// router.delete('/delete', verify, UserController.user_delete)

//edit profile
router.put('/edit/:id', UserController.user_edit_profile)
//update verify status
router.put('/status/:id', UserController.user_verify_status)

// //change password
// router.patch('/password', verify, UserController.user_change_password)


module.exports = router;