import React, {Component} from 'react';
import './Register.css';

import {Form, Input, Button, Icon, notification} from 'antd';
import {signUp} from "../RestClient";

const FormItem = Form.Item;

class Register extends Component {
    render() {
        const AntWrappedRegisterForm = Form.create()(RegisterForm);
        return (
            <div className="register-container">
                <h1 className="page-title">Register</h1>
                <div className="register-content">
                    <AntWrappedRegisterForm/>
                </div>
            </div>
        );
    }
}

class RegisterForm extends Component {

    handleSubmit = (event) => {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const formData = Object.assign({}, values);
                const signUpRequest = {
                    email: formData.email,
                    password: formData.password,
                    role: "PATIENT"
                };
                signUp(signUpRequest)
                    .then(response => {
                        notification.success({
                            message: 'Success',
                            description: 'The user was created successfully.'
                        });
                    })
                    .catch(error => {
                        notification.error({
                            message: 'Something went wrong!',
                            description: 'Your email may be in use or your password doesn\'t range from 6 to 20 characters.'
                        });
                    })
            }
        });
    };

    compareToFirstPassword = (rule, value, callback) => {
        const form = this.props.form;
        if (value && value !== form.getFieldValue('password')) {
            callback('The passwords are not the same!');
        } else {
            callback();
        }
    };

    render() {
        const {getFieldDecorator} = this.props.form;
        return (
            <Form onSubmit={this.handleSubmit} className="register-form">
                <FormItem>
                    {getFieldDecorator('email', {
                        rules: [{required: true, message: 'Please input your email!'}],
                    })(
                        <Input
                            prefix={<Icon type="user"/>}
                            size="large"
                            name="usernameOrEmail"
                            placeholder="Email"/>
                    )}
                </FormItem>
                <FormItem>
                    {getFieldDecorator('password', {
                        rules: [
                            {required: true, message: 'Please input your password!'}
                        ],
                    })(
                        <Input
                            prefix={<Icon type="lock"/>}
                            size="large"
                            name="password"
                            type="password"
                            placeholder="Password"/>
                    )}
                </FormItem>
                <FormItem>
                    {getFieldDecorator('confirmPassword', {
                        rules: [
                            {required: true, message: 'Please input your confirmation password!'},
                            {validator: this.compareToFirstPassword}
                        ],
                    })(
                        <Input
                            prefix={<Icon type="lock"/>}
                            size="large"
                            name="password"
                            type="password"
                            placeholder="Password"/>
                    )}
                </FormItem>
                <FormItem>
                    <Button type="primary" htmlType="submit" size="large"
                            className="register-form-button">Register</Button>
                </FormItem>
            </Form>
        );
    }
}


export default Register;