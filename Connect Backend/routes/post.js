const router = require('express').Router();
const Post = require('../models/Post');

// Create Post
router.post("/create", async function (req, res) {
    const newPost = new Post(req.body);
    console.log(newPost)
    try {
        const Post = await newPost.save();
        res.status(200).send({
            status: 200,
            message: "post created",
          });

    } catch (error) {
        res.status(500).send({
            status: 200,
            message: " creating error",
          });
    }
});

// Update Post
router.put("/:id", async function (req, res) {
    // Validate posts
    try {
        const post = await Post.findById(req.params.id);

        if (post.userId === req.body.userId) {
            await post.updateOne({ $set: req.body })
            res.status(200).send({
                status: 200,
                message: "post updated",
              });
        } else {
            res.status(403).send({
                status: 200,
                message: " error",
              });
        };

    } catch (error) {
        res.status(500).json(error)
    };
});

// Delete Post
router.delete("/:id", async function (req, res) {
    // Validate posts
    try {
        const post = await Post.findById(req.params.id);

        if (post.userId === req.body.userId) {
            await post.deleteOne({ $set: req.body });
            res.status(200).send({
                status: 200,
                message: "post delete",
              });
        } else {
            res.status(403).send({
                status: 200,
                message: "NOt  allowed",
              });
        }
    } catch (error) {
        res.status(500).json(error);
    }
});

// Like and dislike post 
router.put("/:id/like", async function (req, res) {
    try {
        const post = await Post.findById(req.params.id);
        if (!post.likes.includes(req.body.userId)) {
            await post.updateOne({ $push: { likes: req.body.userId } });
            res.status(200).send({
                status: 200,
                message: "liked post",
              });
        } else {
            await post.updateOne({ $pull: { likes: req.body.userId } });
            res.status(200).send({
                status: 200,
                message: "like remove",
              });
        };

    } catch (error) {
        res.status(500).json(error);
    };
});


router.put("/:id/dislike", async function (req, res) {
    try {
        const post = await Post.findById(req.params.id);
        if (!post.likes.includes(req.body.userId)) {
            await post.updateOne({ $push: { dislikes: req.body.userId } });
            res.status(200).send({
                status: 200,
                message: "dislike post",
              });
        } else {
            await post.updateOne({ $pull: { dislikes: req.body.userId } });
            res.status(200).send({
                status: 200,
                message: "remove dislike",
              });
        };

    } catch (error) {
        res.status(500).json(error);
    };
});
//commments
router.put("/:id/comment", async function (req, res) {
    try {
        const post = await Post.findById(req.params.id);
        if (!post.likes.includes(req.body.userId)) {

            // post.comments.unshift(userComment);
            await post.updateOne({ $push: { comments: { user: req.body.userId, comment: req.body.comment } } });
            res.status(200).send({
                status: 200,
                message: "post commented",
              });
        } else {
            // await post.updateOne({$pull: {comments:req.body.userId }});
            // res.status(200).json("Post has been uncommmented");
        }

    } catch (error) {
        res.status(500).json(error);
    };
});

//fetch all post
router.get("/allpost", async function (req, res) {
    try {
        const posts = await Post.find().sort({ time: -1 });

        res.status(200).send(posts);

    } catch (error) {
        res.status(500).json(error);
    };
});
// Fetch Posts
router.get("/:id", async function (req, res) {
    try {
        const post = await Post.findById(req.params.id);
        res.status(200).send(post);

    } catch (error) {
        res.status(500).json(error);
    };
});

// Fetch Time line Posts
router.get("/timeline/more", async function (req, res) {
    try {
        const isCurrentUser = await User.findById(req.body.userId);
        const posts = await Post.find({ userId: isCurrentUser._id });
        const connectionPosts = await Promise.all(
            isCurrentUser.following.map((connectionId) => {
                Post.find({ userId: connectionId });
            }));

        res.json(posts.concat(...connectionPosts));

    } catch (error) {
        res.status(500).json(error);
    };
});

module.exports = router;