const mongoose = require("mongoose");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcrypt");
// const nodemailer = require("nodemailer");
// const { v4: uuidv4 } = require("uuid");
const User = require("../models/User");
const {
  registerValidation,
  loginValidation,
  profileValidation,
} = require("../validation");
const UserVerifcation = require("../models/UserVerification");
const createError = require("http-errors");

//find one user
exports.user_find_one = async (req, res, next) => {
  const { id } = req.params;
  console.log(id);
  //finding user
  try {
    const user = await User.findOne({ _id: id });
    if (user) {
      res.status(200).send(user);
    } else {
      throw createError(404, "Not found");
    }
  } catch (error) {
    next(error);
    return;
  }
};
//edit profile
exports.user_edit_profile = async (req, res, next) => {
  try {
    const { _id, location, weight, phone } = req.body;
    const user = await User.findOneAndUpdate({ _id }, { location, weight, phone })
    if (user) {
      res.status(200).send({
        status: 200,
        message: "Profile updated"
      })
    } else {
      next(404, "User not found")
    }
  } catch (error) {
    next(error)
  }
}
//create Profile
exports.user_create_profile = async (req, res, next) => {
  //console.log("Request recieved");
  const { valid, error } = profileValidation(req.body);
  console.log(req.body.email)
  if (!valid) {
    console.log(error);
    next(createError(400, error));
    return;
  }

  try {
    const { email } = req.body;
    const { gender, dob, phone, branch, imageUrl } = req.body;
    const user = await User.findOneAndUpdate(
      { email },
      { gender, dob, phone, branch, imageUrl }
    );

    if (user) {
      res.status(200).send({
        status: 200,
        message: "Profile created",
      });
    } else {
      throw createError(404, "User not found");
    }
  } catch (error) {
    console.log(error);
    next(error);
  }
};

//getting all users
exports.user_find_all = async (req, res, next) => {
  try {
    const users = await User.find();
    res.send(users);
  } catch (error) {
    next(error);
    return;
  }
};

// registering new user
exports.user_register = async (req, res, next) => {
  //validating user data


  console.log(req.body)
  const { valid, error } = registerValidation(req.body);

  if (!valid) {
    next(createError(400, error));
    return;
  }


  //checking if the user already exsist

  try {
    const emailExist = await User.findOne({ email: req.body.email });

    if (emailExist) {
      throw createError(400, "Email already exist");
      return;
    }
  } catch (err) {
    console.log('error');
    next(err);
    return;
  }

  //hash password
  const salt = await bcrypt.genSalt(10);
  const hashedPassword = await bcrypt.hash(req.body.password, salt);

  //creating new user
  const user = new User({
    name: req.body.name,
    email: req.body.email,
    password: hashedPassword,
    regNo: req.body.regNo
  });
  console.log(user);
  try {
    await user.save();
    res.status(200).send({
      status: 200,
      message: "created"
    })
    // const savedUser = await user.save().then((result) => {
    //   console.log("Sending email");

    //   res.status(200).send("register");
    //   //sendVerificationEmail(result, res);
    // });
  } catch (err) {
    next(err);
    return;
  }
};

//login new user
exports.user_login = async (req, res, next) => {
  //validating user data
  const { valid, error } = loginValidation(req.body);

  if (!valid) {
    next(createError(400, error));
    return;
  }


  //checking if the email exsist
  try {
    const user = await User.findOne({ email: req.body.email });

    if (!user) {
      throw createError(404, "Incorrect Email");
    }

    if (!user.verified) {
      next(createError(401, "Email not verified"));
      return;
    }

    const validPass = await bcrypt.compare(req.body.password, user.password);
    if (!validPass) {
      next(createError(400, "Incorrect password"));
      return;
    }

    await User.updateOne({ email: req.body.email }, { token: req.body.token })

    //create web token
    const token = jwt.sign({ _id: user._id }, process.env.TOKEN_SECRET);
    res.header("auth_token", token).send(user);
  } catch (error) {
    next(error);
    return;
  }
};

//deleting user
exports.user_delete = async (req, res, next) => {
  try {
    const _id = req.query.user_id;
    const user = await User.findOneAndDelete({ _id });
    if (user) {
      res.status(200).send({
        status: 200,
        message: "Account deleted"
      })
    } else {
      next(createError(404, "User not found"))
    }
  } catch (error) {
    next(error)
  }

}