"""
User management module for KodBank
"""

from datetime import datetime
from typing import Optional, List


class User:
    """Represents a bank user/customer"""
    
    def __init__(self, user_id: str, name: str, email: str, phone: str, address: str):
        self.user_id = user_id
        self.name = name
        self.email = email
        self.phone = phone
        self.address = address
        self.created_at = datetime.now()
        self.is_active = True
    
    def __repr__(self):
        return f"User({self.user_id}, {self.name}, {self.email})"
    
    def update_profile(self, name: Optional[str] = None, email: Optional[str] = None, 
                      phone: Optional[str] = None, address: Optional[str] = None):
        """Update user profile information"""
        if name:
            self.name = name
        if email:
            self.email = email
        if phone:
            self.phone = phone
        if address:
            self.address = address
    
    def deactivate(self):
        """Deactivate user account"""
        self.is_active = False
    
    def activate(self):
        """Activate user account"""
        self.is_active = True


class UserManager:
    """Manages user operations"""
    
    def __init__(self):
        self.users: dict = {}
    
    def create_user(self, user_id: str, name: str, email: str, phone: str, address: str) -> User:
        """Create a new user"""
        if user_id in self.users:
            raise ValueError(f"User {user_id} already exists")
        
        user = User(user_id, name, email, phone, address)
        self.users[user_id] = user
        return user
    
    def get_user(self, user_id: str) -> Optional[User]:
        """Retrieve user by ID"""
        return self.users.get(user_id)
    
    def delete_user(self, user_id: str) -> bool:
        """Delete a user"""
        if user_id in self.users:
            del self.users[user_id]
            return True
        return False
    
    def get_all_users(self) -> List[User]:
        """Get all users"""
        return list(self.users.values())
    
    def user_exists(self, user_id: str) -> bool:
        """Check if user exists"""
        return user_id in self.users
