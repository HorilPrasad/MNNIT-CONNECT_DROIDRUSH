const Ajv = require("ajv");
const addFormats = require("ajv-formats");
const ajv = new Ajv();
addFormats(ajv);

const loginValidation = (body) => {
    const schema = {
      type: "object",
      properties: {
        email: {
          type: "string",
          minLength: 6,
          maxLength: 255,
          format: "email",
        },
        password: {
          type: "string",
          minLength: 6,
          maxLength: 1024,
        },
        token: {
          type: "string"
        }
      },
      required: ["email", "password"],
    };
    const valid = ajv.validate(schema, body);
    var error = ajv.errors;
    if (!valid) {
      error = ajv.errors[0].message;
    }
  
    return {
      valid,
      error,
    };
  };

  const registerValidation = (body) => {
    const schema = {
      type: "object",
      properties: {
        name: {
          type: "string",
          minLength: 4,
          maxLength: 255,
        },
        email: {
          type: "string",
          minLength: 6,
          maxLength: 255,
          format: "email",
        },
        password: {
          type: "string",
          minLength: 6,
          maxLength: 1024,
        },
        date: {
          type: "string",
          format: "date",
        },
        regNo: {
          type: "string"
        },
      },
      required: ["name", "email", "regNo", "password"],
    };
    const valid = ajv.validate(schema, body);
    var error = ajv.errors;
    if (!valid) {
      error = ajv.errors[0].message;
    }
  
    return {
      valid,
      error,
    };
  };
  const profileValidation = (body) => {
    const schema = {
      type: "object",
      properties: {
        phone: {
          type: "string",
        },
        gender: {
          type: "string",
        },
        branch: {
          type: "string",
        },
        dob: {
          type: "string",
        }
      },
      required: ["gender","dob","phone","branch"],
    };
    const valid = ajv.validate(schema, body);
    var error = ajv.errors;
    if (!valid) {
      console.log(ajv.errors);
      error = ajv.errors[0].message;
    }
  
    return {
      valid,
      error,
    };
  };

module.exports.registerValidation = registerValidation;
module.exports.loginValidation = loginValidation;
module.exports.profileValidation = profileValidation;