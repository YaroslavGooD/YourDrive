import React, {Component} from 'react';
import './App.css';
import {
    Route,
    withRouter,
    Switch
} from 'react-router-dom';

import AppHeader from '../header/AppHeader';
import LoadingIndicator from '../header/LoadingIndicator';
import Login from '../content/user/Login';
import PrivateRoute from './PrivateRoute'

import {Layout, notification} from 'antd';
import Drive from '../content/Drive';
import Register from "../content/user/Register";

const {Content} = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false
        };

        notification.config({
            placement: 'topRight',
            top: 70,
            duration: 3,
        });
    }

    handleLogin = () => {
        notification.success({
            message: 'Your Drive',
            description: 'You are successfully logged in.',
        });
        this.setState({
            currentUser: {id: 1, username: 'todo', name: 'todo'},
            isAuthenticated: true,
            isLoading: false
        });
        this.props.history.push('/drive');
    };

    handleLogout = (redirectTo = "/", notificationType = "success", description = "You're successfully logged out.") => {
        localStorage.removeItem('accessToken');

        this.setState({
            currentUser: null,
            isAuthenticated: false,
        });

        this.props.history.push(redirectTo);

        notification[notificationType]({
            message: 'Your Drive',
            description: description,
        })
    };

    render() {
        if (this.state.isLoading) {
            return <LoadingIndicator/>
        }
        return (
            <Layout className="app-container">
                <AppHeader isAuthenticated={this.state.isAuthenticated}
                           currentUser={this.state.currentUser}
                           onLogout={this.handleLogout}
                />
                <Content className="app-content">
                    <div className="container">
                        <Switch>
                            <Route path="/login"
                                   render={() => <Login onLogin={this.handleLogin}/>}
                            />
                            <Route path="/register"
                                    component={Register}
                            />
                            <PrivateRoute authenticated={this.state.isAuthenticated}
                                          path="/drive"
                                          component={Drive}
                                          handleLogout={this.handleLogout}
                            />
                        </Switch>
                    </div>
                </Content>
            </Layout>
        );
    }
}

export default withRouter(App);
